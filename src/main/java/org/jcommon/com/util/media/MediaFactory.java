package org.jcommon.com.util.media;

import java.io.File;


public abstract class MediaFactory {
	public abstract Media getMediaFromUrl(String url);
	public abstract File createEmptyFile(Media media);
	public abstract Media createUrl(Media media);
}

