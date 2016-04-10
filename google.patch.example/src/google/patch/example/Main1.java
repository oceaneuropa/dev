package google.patch.example;

import java.util.LinkedList;

import google.patch.example.diff_match_patch.Patch;

public class Main1 {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// ----------------------------------------------------------------------------------
		// Client side
		// ----------------------------------------------------------------------------------
		String clientOldString = "abc";
		String clientNewString = "a123bXYZc";

		System.out.println("clientOldString:");
		System.out.println("---------------------------------------------");
		System.out.println(clientOldString);
		System.out.println("---------------------------------------------");
		System.out.println();

		System.out.println("clientNewString:");
		System.out.println("---------------------------------------------");
		System.out.println(clientNewString);
		System.out.println("---------------------------------------------");
		System.out.println();

		diff_match_patch clientAPI = new diff_match_patch();

		// 1. Create list of Patch with old string and new string
		LinkedList<Patch> clientPatches = clientAPI.patch_make(clientOldString, clientNewString);
		// 2. Convert list of Patch to patch string
		String clientPatchStr = clientAPI.patch_toText(clientPatches);

		System.out.println("patchStr:");
		System.out.println("---------------------------------------------");
		System.out.println(clientPatchStr);
		System.out.println("---------------------------------------------");
		System.out.println();

		// ----------------------------------------------------------------------------------
		// Server side
		// ----------------------------------------------------------------------------------
		String serverOldString = clientOldString;
		String serverPatchStr = clientPatchStr;
		String serverNewString = null;

		diff_match_patch serverAPI = new diff_match_patch();

		// 3. Convert patch string to a list of Patch
		LinkedList<Patch> remotePatches = (LinkedList<Patch>) serverAPI.patch_fromText(serverPatchStr);

		// 4. Apply list of Patch to old string and return new string
		Object[] result = serverAPI.patch_apply(remotePatches, serverOldString);
		serverNewString = (String) result[0];
		// boolean succeed = (boolean) result[1];

		System.out.println("serverOldString:");
		System.out.println("---------------------------------------------");
		System.out.println(serverOldString);
		System.out.println("---------------------------------------------");
		System.out.println();

		System.out.println("remoteNewString:");
		System.out.println("---------------------------------------------");
		System.out.println(serverNewString);
		System.out.println("---------------------------------------------");
		System.out.println();
	}

}
