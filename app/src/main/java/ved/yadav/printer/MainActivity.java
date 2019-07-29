package ved.yadav.printer;

/**
 * Created by ved yadav on 28/07/2019.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends Activity{
    private String TAG = "Main Activity";
    TextView message ;
    Button btnPrint, btnBill;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (TextView)findViewById(R.id.txtMessage);
        message.setText("Synergy Print Demo");
        btnBill = (Button)findViewById(R.id.btnBill);

        btnBill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                printBill();
            }
        });

    }

    protected void printBill() {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);


//                printCustom("Synergy",3,1);

                printPhoto(R.drawable.fillnow_2);

                printNewLine();
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("DISPENSE",1,1);
//                printNewLine();
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);

                printNewLine();
                printCustom("Franchise Name:",0,0);
                printCustom("GSTIN: ",0,0);
                printCustom("Refueller No:",0,0);

//                String dateTime[] = getDateTime();
//                printText(leftRightAlign(dateTime[0], dateTime[1]));
//                printText(leftRightAlign("Qty: Name" , "Price"));
                printNewLine();
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("Transaction Details",1,1);
//                printNewLine();
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printNewLine();

                printCustom("Date:",0,0);
                printCustom("Order ID:",0,0);
                printCustom("Payment Ref No:",0,0);
                printCustom("Location Name/ID:",0,0);
                printCustom("Latitude:",0,0);
                printCustom("Longitude:",0,0);
                printCustom("GPS Status:",0,0);
                printCustom("Terminal ID:",0,0);
                printCustom("Batch no:",0,0);

                printNewLine();
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("SYNERGY GREEN DIESEL/DIESEL",1,1);
//                printNewLine();
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printNewLine();

                printCustom("Asset Name/ID:",0,0);
                printCustom("RFID Status:",0,0);
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("Start Fueling Time:",0,0);
                printCustom("End Fueling Time:",0,0);
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("Txn No:",0,0);
                printCustom("Quantity (in Lts):",0,0);
                printCustom("Rate (in INR/Litre):",0,0);
                printCustom("Amount (in INR):",0,0);
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("Meter Reading:",0,0);
                printCustom("Asset Other Reading:",0,0);
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("<Repeat Asset if delivered to multiple assets, and show Total figs also>",0,0);
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("Location Reading 1:",0,0);
                printCustom("Location Reading 2:",0,0);
                printCustom(new String(new char[32]).replace("\0", "_"),0,1);
                printCustom("Delivered by:",0,0);

                printCustom(new String(new char[32]).replace("\0", "_"),0,1);

                printNewLine();
                printCustom("Thank you  !!!",0,1);

                printCustom(new String(new char[32]).replace("\0", "="),0,1);

                printNewLine();
                printNewLine();
//                printNewLine();
//                printNewLine();
//                printCustom(new String(new char[32]).replace("\0", "."),0,1);




                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
//                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                outputStream.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try{
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                outputStream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = DeviceList.getSocket();
            if(btsocket != null){
                printText(message.getText().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}