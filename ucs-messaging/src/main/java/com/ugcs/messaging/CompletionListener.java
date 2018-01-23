package com.ugcs.messaging;

import java.util.EventListener;

public interface CompletionListener<T> extends EventListener {

	void completed(CompletionEvent<T> event);
}
