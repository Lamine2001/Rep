package com.siemens.windpower.fltp.hanawsclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class TestSSL {
	public static void main(String[] argv) throws Exception {
		Authenticator.setDefault(new MyAuthenticator());

		URL url = new URL(
				"https://demchdc95qx.dc4ca.siemens.de:4300/siemens/FLTP/xs/fltp_outbound/odata/dtt_project.xsodata/DTT_PROJECT?$format=json");

		URLConnection conn = url.openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String str;
		while ((str = in.readLine()) != null) {
			System.out.println(str);
		}
		in.close();
	}
}

class MyAuthenticator extends Authenticator {

	protected PasswordAuthentication getPasswordAuthentication() {
		String promptString = getRequestingPrompt();
		System.out.println(promptString);
		String hostname = getRequestingHost();
		System.out.println(hostname);
		InetAddress ipaddr = getRequestingSite();
		System.out.println(ipaddr);
		int port = getRequestingPort();

		String username = "PU_FLTP";
		String password = "PU_fltp123";

		return new PasswordAuthentication(username, password.toCharArray());
	}
}
