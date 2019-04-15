package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html>");
		out.println("<h1>On Your Server. MME Notebook</h1>");
		out.println("<body>");
		out.println("<form method='post' action='/Notebook/test'>");
		out.println("command : <input type='text' name='MMEcommand' size='100'><br>");
		out.println("<input type='submit' value='ok'><br>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		String MMEcommand = request.getParameter("MMEcommand");
		
		String username = "jaewon";
		String host = "210.94.199.56";
		int port = 2449;
		String password = "rhdiddl1!";
		
		System.out.println("Connecting to "+ host);
		
		com.jcraft.jsch.Session session = null;
		com.jcraft.jsch.Channel channel = null;
		
		
		JSch jsch = new JSch();
		
		try {
			session = jsch.getSession(username, host, port);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("===============");
		session.setPassword(password);
				
		java.util.Properties config = new java.util.Properties();
				
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		
		System.out.println("===============");
				
		try {
			session.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			channel = session.openChannel("exec");
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//DataInputStream dataIn = new DataInputStream(channel.getInputStream());
		//DataOutputStream dataOut = new DataOutputStream(channel.getOutputStream());
		/*
		System.out.println("command");
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(channel.getInputStream()));
		OutputStream toServer = channel.getOutputStream();
		try {
			channel.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		toServer.write((MMEcommand + "\r\n").getBytes());
		toServer.flush();
		
		StringBuilder builder = new StringBuilder();
		
		int count = 0;
		String line = "";
		while(line != null) {
			line = fromServer.readLine();
			builder.append(line).append("\n");
			
			if(line.endsWith(".") || line.endsWith(">")) {
				break;
			}
		}
		String result = builder.toString();
		System.out.println(result);
		
		
		//channel.setInputStream(System.in);
		//channel.setOutputStream(System.out);
		*/
		
		
		ChannelExec channelExec = (ChannelExec) channel;
		
		System.out.println("Connected to " + host);
				
		channelExec.setCommand(MMEcommand);
		channelExec.setInputStream(null);
		channelExec.setErrStream(System.err);
		
		InputStream in = channelExec.getInputStream();
		try {
			channelExec.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		out.println("<h2>결과</h2>");
		out.println("<a href='http://localhost:8080/Notebook/test'>명령어 실행</a>");
		out.println("<h4>실행  : " + MMEcommand +  "</h4>");
		
		try {
			InputStreamReader inputReader = new InputStreamReader(in, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputReader);
			String line = null;
			
			while((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				out.println(line + "<br>");
			}
			bufferedReader.close();
			inputReader.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
		//channel.setInputStream(null);
		/*
		try {
			channel.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] tmp = new byte[2048];
		//String result;
		
		out.println("<h4>결과 </h4>");
		out.println("<br>");
		while(true){
			System.out.println("!!!" + in.available());
			while(in.available()>0){
				int i=in.read(tmp, 0, 2048);
	            if(i<0)break;
	            
	           // result = new String(tmp, 0, i);
	            System.out.print(new String(tmp, 0, i, "UTF-8"));
	            out.print(new String(tmp, 0, i, "UTF-8") + "<br>");
	            //out.println("<br>");
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
		*/
		
		
		if(channelExec != null) {
			channelExec.disconnect();
		}
		if(session != null) {
			session.disconnect();
		}
			
		
	}

}
