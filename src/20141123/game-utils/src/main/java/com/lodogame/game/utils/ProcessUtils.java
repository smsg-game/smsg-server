package com.lodogame.game.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;

public class ProcessUtils {
	/**
	 * 获取进程PID
	 * 
	 * @return
	 */
	public static Long getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Long.parseLong(processName.split("@")[0]);
	}

	public static void outputPid(String path) {
		File file = new File(path);
		BufferedWriter write = null;
		try {
			if (!file.exists()) {
				if (!file.createNewFile()) {
					return;
				}
			}

			if (file.canWrite()) {
				write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				write.write(getPID().toString());
			}
		} catch (IOException e) {
			// TODO 暂时不做任何处理
		} finally {
			IOUtils.closeQuietly(write);
		}
	}
}
