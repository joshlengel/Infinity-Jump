package com.infinityjump.core.state;

@FunctionalInterface
public interface StateSupplier {

	State get();
}
