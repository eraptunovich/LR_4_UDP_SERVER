import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Main{
    public final static int DEFAULT_PORT = 8001;

    public void runServer() throws IOException {
        DatagramSocket serverSocket = null;

        try {
            byte[] receiveData = new byte[1024];
            serverSocket = new DatagramSocket(DEFAULT_PORT);

            System.out.println("UDPServer: Started on " + serverSocket.getLocalAddress() + ":" + serverSocket.getLocalPort());

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

                if (receivedData.equals("QUIT")) {
                    System.out.println("UDPServer: Stopped");
                    break;
                } else if (receivedData.startsWith("CALC:")) {
                    String[] numbers = receivedData.substring(5).split(",");
                    double x = Double.parseDouble(numbers[0]);
                    double y = Double.parseDouble(numbers[1]);
                    double z = Double.parseDouble(numbers[2]);

                    double numerator = Math.sqrt(8 + Math.abs(Math.pow((x + y), 2) + z));
                    double denominator = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
                    double result = numerator / denominator - Math.exp(Math.abs(x - y)) * (Math.pow(Math.tan(z), 2) * Math.pow(Math.abs(z), 1.0 / 5.0));

                    String response = "CALC_RESULT:" + result;
                    byte[] sendData = response.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);
                }
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Main udpSvr = new Main();
            udpSvr.runServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
