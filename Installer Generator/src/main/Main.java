package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.TreeMap;

public class Main {

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Invalid arguments: inst-gen.jar <save-file> <base-dir> <version> [<exclude-dirs>]");
			return;
		}
		
		String save = (String)args[0];
		String dir = (String)args[1];
		
		String version = (String)args[2];
		
		File saveFile = new File(save);
		
		if (saveFile.isDirectory()) {
			System.out.println("Invalid argument: <save-file> must be a file, not a directory");
			return;
		}
		
		File dirFile = new File(dir);
		
		if (!dirFile.isDirectory()) {
			System.out.println("Invalid argument: <base-dir> must be a directory");
			return;
		}
		
		File[] excludeDirs = new File[args.length - 3];
		
		for (int i = 3; i < args.length; ++i) {
			excludeDirs[i - 3] = new File(dirFile, args[i]);
		}
		
		try {
			saveFile.createNewFile();
		} catch (IOException e) {
			System.out.println("Error creating <save file>:");
			e.printStackTrace();
		}
		
		TreeMap<String, List<String>> directories = new TreeMap<>();
		
		String basePath = dirFile.getAbsolutePath();
		int startAt = basePath.length() + 1;
		addSubFiles(dirFile, directories, startAt, excludeDirs);
		
		try (FileWriter writer = new FileWriter(saveFile)) {
			// Write regular info
			writer.write(
					"!define PRODUCT_NAME \"Infinity Jump IDE\"\r\n" + 
					"!define PRODUCT_VERSION \"" + version + "\"\r\n" + 
					"!define PRODUCT_PUBLISHER \"Josh Lengel\"\r\n" + 
					"!define PRODUCT_DIR_REGKEY \"Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\Infinity Jump IDE.exe\"\r\n" + 
					"!define PRODUCT_UNINST_KEY \"Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${PRODUCT_NAME}\"\r\n" + 
					"!define PRODUCT_UNINST_ROOT_KEY \"HKLM\"\r\n" + 
					"\r\n" + 
					"; MUI 1.67 compatible ------\r\n" + 
					"!include \"MUI.nsh\"\r\n" + 
					"\r\n" + 
					"; MUI Settings\r\n" + 
					"!define MUI_ABORTWARNING\r\n" + 
					"!define MUI_ICON \"${NSISDIR}\\Contrib\\Graphics\\Icons\\modern-install.ico\"\r\n" + 
					"!define MUI_UNICON \"${NSISDIR}\\Contrib\\Graphics\\Icons\\modern-uninstall.ico\"\r\n" + 
					"\r\n" + 
					"; Welcome page\r\n" + 
					"!insertmacro MUI_PAGE_WELCOME\r\n" + 
					"; License page\r\n" + 
					"!insertmacro MUI_PAGE_LICENSE \"" + basePath + "\\LICENSE.txt" + "\"\r\n" + 
					"; Directory page\r\n" + 
					"!insertmacro MUI_PAGE_DIRECTORY\r\n" + 
					"; Instfiles page\r\n" + 
					"!insertmacro MUI_PAGE_INSTFILES\r\n" + 
					"; Finish page\r\n" + 
					"!define MUI_FINISHPAGE_RUN \"$DESKTOP\\Infinity Jump IDE.lnk\"\r\n" + 
					"!define MUI_FINISHPAGE_SHOWREADME \"$INSTDIR\\README.txt\"\r\n" + 
					"!insertmacro MUI_PAGE_FINISH\r\n" + 
					"\r\n" + 
					"; Uninstaller pages\r\n" + 
					"!insertmacro MUI_UNPAGE_INSTFILES\r\n" + 
					"\r\n" + 
					"; Language files\r\n" + 
					"!insertmacro MUI_LANGUAGE \"English\"\r\n" + 
					"\r\n" + 
					"; MUI end ------\r\n" + 
					"\r\n" + 
					"Name \"${PRODUCT_NAME} ${PRODUCT_VERSION}\"\r\n" + 
					"OutFile \"Infinity Jump IDE-" + version + "-setup.exe\"\r\n" + 
					"InstallDir \"$PROGRAMFILES64\\Infinity Jump IDE\"\r\n" + 
					"InstallDirRegKey HKLM \"${PRODUCT_DIR_REGKEY}\" \"\"\r\n" + 
					"ShowInstDetails show\r\n" + 
					"ShowUnInstDetails show\r\n" + 
					"\r\n" + 
					"Section \"Launch\" SEC01\r\n" +
					"  SetOutPath \"$INSTDIR\\bin\"\r\n" +
					"  SetOverwrite try\r\n" + 
					"  CreateDirectory \"$SMPROGRAMS\\Infinity Jump IDE\"\r\n" + 
					"  CreateShortCut \"$SMPROGRAMS\\Infinity Jump IDE\\Infinity Jump IDE.lnk\" \"$INSTDIR\\bin\\Infinity Jump IDE.exe\"\r\n" + 
					"  CreateShortCut \"$DESKTOP\\Infinity Jump IDE.lnk\" \"$INSTDIR\\bin\\Infinity Jump IDE.exe\"\r\n");
			
			if (directories.containsKey("bin")) {
				for (String file : directories.get("bin")) {
					writer.write("  File \"" + file + "\"\r\n");
				}
			}
			
			writer.write(
					"SectionEnd\r\n" +
					"\r\n" +
					"Section \"Core\" SEC02\r\n");
	
			for (Entry<String, List<String>> entry : directories.entrySet()) {
				if (entry.getValue().isEmpty()) continue;
				
				if (entry.getKey().isEmpty()) {
					writer.write("  SetOutPath \"$INSTDIR\"\r\n");
				} else {
					writer.write("  SetOutPath \"$INSTDIR\\" + entry.getKey() + "\"\r\n");
				}
				
				for (String file : entry.getValue()) {
					writer.write("  File \"" + file + "\"\r\n");
				}
			}
			
			writer.write(
					"SectionEnd\r\n" +
					"\r\n");
			
			writer.write(
					"Section -AdditionalIcons\r\n" + 
					"  SetOutPath $INSTDIR\r\n" + 
					"  CreateShortCut \"$SMPROGRAMS\\Infinity Jump IDE\\Uninstall.lnk\" \"$INSTDIR\\uninst.exe\"\r\n" + 
					"SectionEnd\r\n" + 
					"\r\n" + 
					"Section -Post\r\n" + 
					"  WriteUninstaller \"$INSTDIR\\uninst.exe\"\r\n" + 
					"  WriteRegStr HKLM \"${PRODUCT_DIR_REGKEY}\" \"\" \"$INSTDIR\\bin\\Infinity Jump IDE.exe\"\r\n" + 
					"  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"DisplayName\" \"$(^Name)\"\r\n" + 
					"  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"UninstallString\" \"$INSTDIR\\uninst.exe\"\r\n" + 
					"  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"DisplayIcon\" \"$INSTDIR\\bin\\Infinity Jump IDE.exe\"\r\n" + 
					"  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"DisplayVersion\" \"${PRODUCT_VERSION}\"\r\n" + 
					"  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\" \"Publisher\" \"${PRODUCT_PUBLISHER}\"\r\n" + 
					"SectionEnd\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"Function un.onUninstSuccess\r\n" + 
					"  HideWindow\r\n" + 
					"  IfSilent +2\r\n" + 
					"  MessageBox MB_ICONINFORMATION|MB_OK \"$(^Name) was successfully removed from your computer.\"\r\n" + 
					"FunctionEnd\r\n" + 
					"\r\n" + 
					"Function un.onInit\r\n" + 
					"  IfSilent +3\r\n" + 
					"  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 \"Are you sure you want to completely remove $(^Name) and all of its components?\" IDYES +2\r\n" + 
					"  Abort\r\n" + 
					"FunctionEnd\r\n" +
					"\r\n" +
					"Section Uninstall\r\n");
			
			for (Entry<String, List<String>> entry : directories.entrySet()) {
				for (String file : entry.getValue()) {
					writer.write("  Delete \"$INSTDIR\\" + file.substring(startAt) + "\"\r\n");
				}
			}
			
			writer.write("\r\n");
			
			writer.write(
					"  Delete \"$INSTDIR\\uninst.exe\"\r\n" +
					"  Delete \"$SMPROGRAMS\\Infinity Jump IDE\\Uninstall.lnk\"\r\n" + 
					"  Delete \"$DESKTOP\\Infinity Jump IDE.lnk\"\r\n" + 
					"  Delete \"$SMPROGRAMS\\Infinity Jump IDE\\Infinity Jump IDE.lnk\"\r\n" +
					"\r\n" +
					"  RMDir \"$SMPROGRAMS\\Infinity Jump IDE\"\r\n");
			
			NavigableSet<String> keys = directories.descendingKeySet();
			
			for (String folder : keys) {
				if (folder.isEmpty()) {
					writer.write("  RMDir \"$INSTDIR\"\r\n");
				} else {
					writer.write("  RMDir \"$INSTDIR\\" + folder + "\"\r\n");
				}
			}
			
			writer.write(
					"  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} \"${PRODUCT_UNINST_KEY}\"\r\n" + 
					"  DeleteRegKey HKLM \"${PRODUCT_DIR_REGKEY}\"\r\n" + 
					"  SetAutoClose true\r\n" + 
					"SectionEnd");
	
		} catch (IOException e) {
			System.out.println("Error writing installer:");
			e.printStackTrace();
			return;
		}
	}
	
	private static void addSubFiles(File root, Map<String, List<String>> m, int startAt, File[] excludeDirs) {
		String path = root.getAbsolutePath();
		File[] files = root.listFiles();
		
		if (path.length() < startAt) path += '\\';
		
		List<String> names = new ArrayList<>();
		
		System.out.println("Current directory: " + path);
		
		for (File f : files) {
			if (contains(excludeDirs, f)) continue;
			
			if (f.isDirectory()) {
				addSubFiles(f, m, startAt, excludeDirs);
			} else {
				names.add(f.getAbsolutePath());
			}
		}

		m.put(path.substring(startAt), names);
	}
	
	private static boolean contains(File[] arr, File f) {
		for (File other : arr) {
			if (other.equals(f)) return true;
		}
		
		return false;
	}
}
