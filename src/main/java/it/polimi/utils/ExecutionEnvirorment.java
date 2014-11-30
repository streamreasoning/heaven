package it.polimi.utils;

import it.polimi.main.CommonMain;

public class ExecutionEnvirorment {

	public static boolean memoryAnalysisEnabled() {
		return CommonMain.CURRENTENGINE != CommonMain.JENANM && CommonMain.CURRENTENGINE != CommonMain.JENANWM
				&& CommonMain.CURRENTENGINE != CommonMain.JENARHODFNM && CommonMain.CURRENTENGINE != CommonMain.JENARHODFNWM
				&& CommonMain.CURRENTENGINE != CommonMain.PLAIN2369NM && CommonMain.CURRENTENGINE != CommonMain.PLAIN2369NWM;
	}

	public static boolean isWritingProtected() {
		return CommonMain.CURRENTENGINE == CommonMain.JENANW || CommonMain.CURRENTENGINE == CommonMain.JENARHODFNW
				|| CommonMain.CURRENTENGINE == CommonMain.PLAIN2369NW || CommonMain.CURRENTENGINE == CommonMain.JENANWM
				|| CommonMain.CURRENTENGINE == CommonMain.JENARHODFNWM || CommonMain.CURRENTENGINE == CommonMain.PLAIN2369NWM;
	}

}
