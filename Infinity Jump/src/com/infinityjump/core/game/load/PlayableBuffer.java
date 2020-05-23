package com.infinityjump.core.game.load;

import java.util.concurrent.ArrayBlockingQueue;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.game.level.Level;
import com.infinityjump.core.game.level.LevelStream;
import com.infinityjump.core.game.script.Script;
import com.infinityjump.core.game.script.ScriptStream;
import com.infinityjump.core.utils.GlobalProperties;

public class PlayableBuffer {

	private static int BUFFER_SIZE;
	
	public static void init() {
		BUFFER_SIZE = Integer.parseInt(GlobalProperties.properties.getProperty("level-buffer-size"));
	}
	
	private volatile LevelStream levelStream;
	private volatile ScriptStream scriptStream;
	private volatile ArrayBlockingQueue<Playable> queue;
	
	private volatile Object lock;
	
	private class LoaderThread extends Thread {
		
		@Override
		public void run() {
			while (true) {
				if (queue.remainingCapacity() > 0) {
					Level l = levelStream.next();
					Script s = scriptStream.next();
					
					if (l == null || s == null) break; // finish loading and let remaining items in queue drain
					
					queue.add(new Playable(l, s));
				} else {
					try {
						synchronized(lock) {
							lock.wait(); // wait until next element is taken
						}
					} catch (InterruptedException e) {
						Logger.getAPI().log("Level loading thread interrupted");
					}
				}
			}
		}
		
		boolean hasNext() {
			return !queue.isEmpty() || (levelStream.hasNext() && scriptStream.hasNext());
		}
	}
	
	private LoaderThread loader;
	
	public PlayableBuffer(LevelStream levelStream, ScriptStream scriptStream) {
		this.levelStream = levelStream;
		this.scriptStream = scriptStream;
		this.queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
		
		this.lock = new Object();
		
		this.loader = new LoaderThread();
	}
	
	public void start() {
		loader.start();
	}
	
	public Playable get() {
		if (loader.hasNext()) {
			try {
				return queue.take();
			} catch (InterruptedException e) {
				Logger.getAPI().error("Error loading next level");
				return null;
			} finally {
				synchronized(lock) {
					lock.notify();
				}
			}
		} else {
			return null;
		}
	}
	
	public boolean hasNext() {
		return loader.hasNext();
	}
}