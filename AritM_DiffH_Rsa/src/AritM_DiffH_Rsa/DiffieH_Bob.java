package AritM_DiffH_Rsa;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class DiffieH_Bob {
	public static void main(String[] args) throws IOException {
		Socket bobSocket = null; 
		String userInput;
		String aliceResponse;
		PrintWriter out = null; 
		BufferedReader in = null;
		InetAddress ipExchange;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		BigInteger sBInt = BigInteger.valueOf(0);
		BigInteger p, b, sB, key, keyAB; 
		// primo, base, secreto de Alice, chave que Bob envia a Alice, chave compartilhada entre Bob e Alice;

        //caso usuário nao passar IP do servidor da troca na inicialização, pede para informar.
        if (args.length > 0){
            ipExchange = InetAddress.getByName(args[0]);
        }else{ //se usuário não informou na chamada do programa, mostra mensagem 
            System.out.print("Enter the IP that exchanged the key: ");
            ipExchange = InetAddress.getByName(stdIn.readLine());
        }
           
        System.out.println ("Connecting in " + ipExchange + " port 13131.");

        try {       
            bobSocket = new Socket(ipExchange, 13131);
            out = new PrintWriter(bobSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + ipExchange);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error I/O in: " + ipExchange);
            System.exit(1);
        }

        System.out.print("Enter prime number = ");
        p=sc.nextBigInteger();
 
        System.out.print("Enter the base = ");
        b=sc.nextBigInteger();
            
        System.out.print("Enter the Bob's secret = ");
        sB=sc.nextBigInteger();

    	key = opAritmeticas.exponenciacao(b, sB, p);
    		
    	System.out.println("Key sent to Alice = "+key);
    		
    	String keyB = key+"";
        out.println(keyB); //manda para a Alice

        while(true){
            aliceResponse = in.readLine(); //lê resposta da Alice
            if(aliceResponse.contentEquals(".")){
                System.out.println("Alice closed connection!");
                break;
            }else{
                System.out.println("Response: " + aliceResponse);
                break;
            }
        }

        sBInt = sBInt.add(BigInteger.valueOf(Long.parseLong(aliceResponse)));
		
        keyAB = opAritmeticas.exponenciacao(sBInt, sB, p);
		System.out.println("The key shared between Alice and Bob is: "+keyAB);
            
		while(true){
            System.out.println("Enter '.' to close connection!");
            userInput = stdIn.readLine();
            if(userInput.equals(".")){//finaliza conexão
            	System.out.println("Connection closed!");
            	out.println(".");
            	break;
			}
        }   
	
		out.close();
		in.close();
		stdIn.close();
		sc.close();
		bobSocket.close();
    }
}
