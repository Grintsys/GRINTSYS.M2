package grintsys.com.vanshop.utils;

/**
 * Created by turupawn on 3/24/17.
 */

import android.app.Activity;
import android.content.Context;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import datamaxoneil.connection.ConnectionBase;
import datamaxoneil.connection.Connection_Bluetooth;
import datamaxoneil.printer.DocumentExPCL_LP;
import datamaxoneil.printer.ParametersExPCL_LP;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.order.OrderItem;
import grintsys.com.vanshop.entities.invoice.Invoice;
import grintsys.com.vanshop.entities.payment.InvoiceItem;

public class BluetoothPrinterPayment {

    public static String print(Context context,
                               String bluetooth_mac_address,
                               String date,
                               Client client,
                               String seller,
                               List<InvoiceItem> invoices,
                               double sub_total,
                               double discount,
                               // double total_after_discount,
                               String comment,
                               //double IVA,
                               String docEntry,
                               double total)
    {
        byte[] printData = {0};

        DocumentExPCL_LP docExPCL_LP = new DocumentExPCL_LP(3);
        ParametersExPCL_LP paramExPCL_LP = new ParametersExPCL_LP();

        //Formulado. con variable comment
        String TipoPago="";
        String Reference ="";
        String Documento = "";
        String TotaDocumento = "9999";

        comment = comment.toLowerCase();

        if (comment.contains("cheque")==true)
        {
            TipoPago = "Cheque";
        }
        else if (comment.contains("deposito")==true)
        {
            TipoPago = "Deposito";
        }
        else if (comment.contains("efectivo")==true)
        {
            TipoPago = "Efectivo";
        }
        else if (comment.contains("transferencia")==true)
        {
            TipoPago = "Transferencia";
        }

        Reference = comment.replace(TipoPago.toLowerCase(),"").replace(" ","");

        //Titulo
        paramExPCL_LP.setFontIndex(1);
        paramExPCL_LP.setIsBold(true);
        docExPCL_LP.writeText("     Recibo de Pago", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);

        //Encabezado
        paramExPCL_LP.setFontIndex(1);
        paramExPCL_LP.setIsBold(true);
        docExPCL_LP.writeText("VAN HEUSEN DE C.A.", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        paramExPCL_LP.setFontIndex(5);
        paramExPCL_LP.setIsBold(false);
        docExPCL_LP.writeText("Col San Fernando. Ave. Juan Pablo II frente a la Leyde. SPS Cortes", paramExPCL_LP);
        docExPCL_LP.writeText("Tel: " + "25160100" + " " + "Fax: " + "25164080", paramExPCL_LP);
        paramExPCL_LP.setFontIndex(3);
        docExPCL_LP.writeText("RTN: " + "05019995143200", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        //docExPCL_LP.writeText("", paramExPCL_LP);

        paramExPCL_LP.setFontIndex(2);
        docExPCL_LP.writeText("Recibo No.:          " + docEntry, paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);

        paramExPCL_LP.setFontIndex(5);
        docExPCL_LP.writeText("Fecha y Hora" + ":   " + date, paramExPCL_LP);
        docExPCL_LP.writeText("Cliente" + ":        " + client.getName(), paramExPCL_LP);
        docExPCL_LP.writeText("RTN:                 " + client.getRTN(), paramExPCL_LP);

        paramExPCL_LP.setIsUnderline(true);
        //48 spaces.
        docExPCL_LP.writeText("                                                ", paramExPCL_LP);
        paramExPCL_LP.setIsUnderline(false);
        docExPCL_LP.writeText("         Detalle de lo Pagado", paramExPCL_LP);
        paramExPCL_LP.setIsUnderline(true);
        docExPCL_LP.writeText("Factura                 |       Monto           ", paramExPCL_LP);
        // docExPCL_LP.writeText("1600001573              |       300000.00           ", paramExPCL_LP);
        //paramExPCL_LP.setIsUnderline(true);
        //docExPCL_LP.writeText("", paramExPCL_LP);
        DecimalFormat df = new DecimalFormat("0.00");
        //paramExPCL_LP.setIsUnderline(false);

        //  try {
        //      Thread.sleep(2000);
        //  } catch (InterruptedException e) {
        //      e.printStackTrace();
        //  }

        try {
            //Thread.sleep(2000);
            if(invoices.size() > 0 ) {

                docExPCL_LP.writeText(invoices.get(0).getDocumentNumber() + "         " + invoices.get(0).getTotalAmount());

            }

        } catch (Error e) {
            //    e.printStackTrace();
        }

        //docExPCL_LP.writeText(invoices.get(0).getDocumentNumber() +
        //        '\t' + invoices.get(0).getTotalAmount());

        //DEM, August 14TH- 2017. Aca iria el loop de las facturas.
        //if(invoices!=null) {
        /*
            for (int i = 0; i < invoices.size(); i++)
            {

                //try {
                //    Thread.sleep(2000);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}

                //try {
                //    TimeUnit.SECONDS.sleep(10);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}

                //Documento = invoices.get(i).getDocumentNumber();

                //docExPCL_LP.writeText(invoices.get(i).getDocumentNumber() + "         " + invoices.get(i).getPayedAmount());
                //docExPCL_LP.writeText( Documento+ "     " + df.format(invoices.get(i).getTotalAmount()), paramExPCL_LP);
               // docExPCL_LP.writeText(TotaDocumento, paramExPCL_LP);

                docExPCL_LP.writeText(invoices.get(i).getDocumentNumber() +
                        '\t' + invoices.get(i).getPayedAmount());


            }
        //}

        */

        paramExPCL_LP.setIsUnderline(true);
        docExPCL_LP.writeText("                                                ", paramExPCL_LP);

        paramExPCL_LP.setIsUnderline(false);
        paramExPCL_LP.setIsBold(true);
        docExPCL_LP.writeText("         Total Pagado: \t\t"+ df.format(total), paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        paramExPCL_LP.setIsBold(false);
        //docExPCL_LP.writeText("Forma de Pago:", paramExPCL_LP);
        docExPCL_LP.writeText("Forma de Pago" + ":               " + TipoPago, paramExPCL_LP);

        if (TipoPago != "Efectivo")
        {
            docExPCL_LP.writeText("Numero de " + TipoPago + ":     " + Reference, paramExPCL_LP);
        }

        docExPCL_LP.writeText("Vendedor" + ":                    " + seller, paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);

        paramExPCL_LP.setIsBold(true);
        docExPCL_LP.writeText("Gracias por su Pago", paramExPCL_LP);

        paramExPCL_LP.setIsUnderline(false);
        docExPCL_LP.writeText("", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);
        docExPCL_LP.writeText("", paramExPCL_LP);

        printData = docExPCL_LP.getDocumentData();

        try {
            ConnectionBase conn = Connection_Bluetooth.createClient(bluetooth_mac_address);
            if(conn.open()){
                conn.write(printData);
                conn.close();
            }
        } catch (Exception e) {
            //e.printStackTrace();

            MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, e.getMessage(), MsgUtils.ToastLength.LONG);
            return e.getMessage().toString();

        }

        return "";

    }



}
