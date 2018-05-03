package net.continuumsecurity.sslCertificate;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import net.continuumsecurity.scanner.ZapManager;

/*
 * Pour gradle : 
 * 
 * compile 'org.apache.httpcomponents:httpclient:4.5.5'
 * */
public class SSLCertificateEnabled {
	
	private final static Logger log = Logger.getLogger(SSLCertificateEnabled.class.getName());

	public SSLCertificateEnabled(String zapPort) {
		super();
		this.zapPort=zapPort;
	}
	
	private String zapPort;
	private String cert;
	
	public String getCert() {
		return cert;
	}
	 
	public void setCert(String cert) {
		this.cert = cert;
	}
	
	public void getCertFromZap(){
		String url = "http://localhost:" + zapPort + "/OTHER/core/other/rootcert/";
		CloseableHttpResponse response= null ;
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		try {
			response = httpClient.execute(httpGet);
		    System.out.println(response.toString());
		    log.info(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/* curl http://localhost:8080/OTHER/core/other/rootcert/  */
}
