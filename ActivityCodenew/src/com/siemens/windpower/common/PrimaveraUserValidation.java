package com.siemens.windpower.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;

import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;

public class PrimaveraUserValidation {
	static Logger logger = null;
	private static final String HTTP = DTTConstants.HTTP;
	private List<String> cookieHeaders = null;
	private Config config = null;

	private static PrimaveraUserValidation uservalidation = new PrimaveraUserValidation();

	public PrimaveraUserValidation() {
		logger = Logger.getLogger(PrimaveraUserValidation.class);
		config = Config.CONFIG;

	}

	public static PrimaveraUserValidation setCookieOrUserTokenData(Client client) {
		uservalidation.setCookieOrUserTokenDataIntenal(client);
		//logger.info("getOutInterceptors sad "+uservalidation);
		return uservalidation;
	}

	private void setCookieOrUserTokenDataIntenal(Client client) {
		System.setProperty("javax.net.ssl.trustStore","D:/keystore/fltp.jks");
		HTTPConduit httpConduit = (HTTPConduit)client.getConduit();
		TLSClientParameters tlsParams = new TLSClientParameters();
		tlsParams.setSecureSocketProtocol("SSL");
		httpConduit.setTlsClientParameters(tlsParams);
		
		logger.info("tlsParams "+tlsParams.getSecureSocketProtocol());
		// Uncomment the following two lines to log SOAPMessages
		client.getEndpoint().getOutInterceptors()
				.add(new LoggingOutInterceptor());
	//	logger.info("getOutInterceptors "+client.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor()));
		client.getEndpoint().getInInterceptors()
				.add(new LoggingInInterceptor());
		/*logger.info("getInInterceptors "+client.getEndpoint().getInInterceptors()
				.add(new LoggingInInterceptor()));*/
		if (config.authMode == USERNAME_TOKEN_MODE
				|| config.authMode == SAML_11_MODE
				|| config.authMode == SAML_20_MODE) {
			client.getEndpoint().getOutInterceptors()
					.add(new SAAJOutInterceptor());
			client.getEndpoint().getInInterceptors()
					.add(new SAAJInInterceptor());
			/*logger.info("SAAJOutInterceptor "+client.getEndpoint().getOutInterceptors().add(new SAAJOutInterceptor()));
					logger.info("SAAJInInterceptor "+client.getEndpoint().getInInterceptors()
							.add(new SAAJInInterceptor())
					);*/
			// To do UsernameToken or SAML, we use our own Interceptor
			// This will also handle encryption, if enabled
			client.getEndpoint().getOutInterceptors()
					.add(new OutInterceptor(config));
			
			/*logger.info("Config1 "+client.getEndpoint().getOutInterceptors()
					.add(new OutInterceptor(config))
			);*/

			// However, we only need a custom inbound Interceptor if we know
			// that the server
			// is sending back encrypted messages.
			if (config.encEnabled && config.encInbound) {
				client.getEndpoint().getInInterceptors()
						.add(new InInterceptor());
				/*logger.info("Config "+client.getEndpoint().getInInterceptors()
						.add(new InInterceptor())
				);*/
			}
		} else {
			HTTPConduit httpConduit1 = (HTTPConduit) client.getConduit();
			HTTPClientPolicy policy = httpConduit.getClient();
			policy.setCookie(cookieHeaders.get(0));
		}
		//logger.info("END "+client);
	}

	public static String makeHttpURLString(String suffix) {

		String httpurl = uservalidation.makeHttpURLStringInternal(suffix);
		//logger.info("httpurl "+httpurl);
		return httpurl;

	}

	private String makeHttpURLStringInternal(String suffix) {
		String hostname = config.hostname;
		int port = config.port;
		StringBuilder sb = new StringBuilder(HTTP);
		sb.append(hostname).append(":").append(port).append(suffix);
		/*logger.info("sb "+hostname);
		logger.info("port "+port);
		logger.info("sb "+sb);*/
		return sb.toString();
	}

	// ~ Inner Classes
	// ------------------------------------------------------------------------------

	public static final int USERNAME_TOKEN_MODE = 0;
	public static final int SAML_11_MODE = 1;
	public static final int SAML_20_MODE = 2;
	public static final int COOKIE_MODE = 3;

	static class Config {
		// ~ Static fields/initializers
		// -------------------------------------------------------------

		public static Config CONFIG = null;

		static {
			ReadProperties properties;
			try {
				properties = new ReadProperties();

				Map propmap;

				propmap = properties.readPropertiesFile();

				CONFIG = new Config(
						propmap.get(DTTConstants.WEBSERVICEUSERNAME).toString(),
						propmap.get(DTTConstants.WEBSERVICEPASSWORD).toString(),
						propmap.get(DTTConstants.PRIMAVERAWEBSERVICESERVER)
								.toString(), Integer.parseInt(propmap.get(
								DTTConstants.WEBSERVICEPORT).toString()),
						DTTConstants.FLTP, false, null, false, false, false,
						new File(DTTConstants.KEYSTOREFILENAME),
						DTTConstants.JKS, DTTConstants.FLTP123,
						DTTConstants.MYKEYS, false, USERNAME_TOKEN_MODE, false,
						null, "JKS", DTTConstants.SAMLALIAS,
						DTTConstants.STOREPASS, DTTConstants.SAMLKEYPASS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// ~ Instance fields
		// ------------------------------------------------------------------------

		final String username;
		final String password;
		final String hostname;
		final int port;
		final String projectId;
		final boolean bPerformExport;
		final File saveTo;
		final boolean bAsync;
		final boolean encEnabled;
		final boolean sigEnabled;
		final File keystore;
		final String keystoreType;
		final String keystorePass;
		final String certAlias;
		final boolean encInbound;
		final int authMode;
		final boolean samlSigned;
		final File samlKeystore;
		final String samlKeystoreType;
		final String samlKeystorepass;
		final String samlKeypass;
		final String samlAlias;

		// ~ Constructors
		// ---------------------------------------------------------------------------

		Config(String username, String password, String hostname, int port,
				String projectId, boolean bPerformExport, File saveTo,
				boolean bAsync, boolean encEnabled, boolean sigEnabled,
				File keystore, String keystoreType, String keystorePass,
				String certAlias, boolean encInbound, int authMode,
				boolean samlSigned, File samlKeystore, String samlKeystoreType,
				String samlAlias, String samlKeystorePass, String samlKeyPass) {
			this.username = username;
			this.password = password;
			this.hostname = hostname;
			this.port = port;
			this.projectId = projectId;
			this.bPerformExport = bPerformExport;
			this.saveTo = saveTo;
			this.bAsync = bAsync;
			this.encEnabled = encEnabled;
			this.sigEnabled = sigEnabled;
			this.keystore = keystore;
			this.keystoreType = keystoreType;
			this.keystorePass = keystorePass;
			this.certAlias = certAlias;
			this.encInbound = encInbound;
			this.authMode = authMode;
			this.samlSigned = samlSigned;
			this.samlKeystore = samlKeystore;
			this.samlKeystoreType = samlKeystoreType;
			this.samlKeystorepass = samlKeystorePass;
			this.samlKeypass = samlKeyPass;
			this.samlAlias = samlAlias;
		}
	}

}
