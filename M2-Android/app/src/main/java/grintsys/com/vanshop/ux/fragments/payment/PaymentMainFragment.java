package grintsys.com.vanshop.ux.fragments.payment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.SettingsMy;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.Bank;
import grintsys.com.vanshop.entities.User.User;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.client.Document;
import grintsys.com.vanshop.entities.order.Order;
import grintsys.com.vanshop.entities.payment.AddPaymentResponse;
import grintsys.com.vanshop.entities.payment.AddPaymentResult;
import grintsys.com.vanshop.entities.payment.InvoiceItem;
import grintsys.com.vanshop.entities.payment.Payment;
import grintsys.com.vanshop.entities.tenant.Tenant;
import grintsys.com.vanshop.listeners.OnSingleClickListener;
import grintsys.com.vanshop.utils.BluetoothPrinterPayment;
import grintsys.com.vanshop.utils.JsonUtils;
import grintsys.com.vanshop.utils.MsgUtils;
import grintsys.com.vanshop.utils.SortByOverDueDays;
import grintsys.com.vanshop.ux.MainActivity;

import timber.log.Timber;

import static com.itextpdf.forms.xfdf.XfdfConstants.DEST;
import static com.itextpdf.html2pdf.html.AttributeConstants.SRC;
import static grintsys.com.vanshop.SettingsMy.getActiveUser;
import static grintsys.com.vanshop.utils.Utils.currentDateToString;
import static grintsys.com.vanshop.utils.Utils.dateToString;


public class PaymentMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CARDCODE = "cardcode";
    private static final String ARG_CLIENT = "client-payment";
    private static final String ARG_PAYMENT = "payment";
    private static final String[] paymentTypes = new String[]{"Efectivo","Cheque","Transferencia","Efectivo Dolar $","Máquina POS"};

    // TODO: Rename and change types of parameters
    private String mCardCode;

    private PaymentMainFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProgressBar progressView;
    protected TextView cashText, transferText, checkText, totalText, totalInvoiceText;
    private Client client;
    private Payment payment;
    private Button paymentSaveButton, paymentSentToSapButton, paymentCancelButton, paymentPrintButton;


    double TotalFacturas=0;
    boolean PagoInvalido = true;
    String docs;
    String CardCode;
    Double Balance;
    private ArrayList<Document> invoiceList;

    String totalString="";
    private Order order;
    public Context context;

    private OnFragmentInteractionListener mListener;

    public PaymentMainFragment() {
        // Required empty public constructor
    }

    /*
    public static PaymentMainFragment newInstance(Payment payment) {
        PaymentMainFragment fragment = new PaymentMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAYMENT, payment);
        fragment.setArguments(args);
        return fragment;
    }*/

    public static PaymentMainFragment newInstance(Client client) {
        PaymentMainFragment fragment = new PaymentMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT, client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardCode = getArguments().getString(ARG_CARDCODE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        setContentVisible(CONST.VISIBLE.CONTENT);
    }

    private void setContentVisible(CONST.VISIBLE visible) {
        if (progressView != null) {
            switch (visible) {
                case PROGRESS:
                    progressView.setVisibility(View.VISIBLE);
                    break;
                case CONTENT:
                    progressView.setVisibility(View.GONE);
                default: // Content
                    progressView.setVisibility(View.GONE);
            }
        } else {
            Timber.e(new RuntimeException(), "Setting content visibility with null views.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_main, container, false);

        MainActivity.setActionBarTitle(getString(R.string.Payments));
        progressView = view.findViewById(R.id.payment_progress);

        //agregado Sept. 27.2017. Issues con invoices de pago anterior.    DEM.
        //((MainActivity) getActivity()).ClearPaymentData();

        paymentSentToSapButton = view.findViewById(R.id.product_payment_main_sent_to_sap);
        paymentSaveButton = view.findViewById(R.id.product_payment_main_save);
        paymentCancelButton = view.findViewById(R.id.product_payment_main_cancel);
        paymentPrintButton = view.findViewById(R.id.product_payment_main_print);

        paymentSentToSapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPaymentToSap();
                ((MainActivity) getActivity()).onAccountSelected();
                MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.PaymentProcess), MsgUtils.ToastLength.SHORT);
            }
        });

        paymentSaveButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                try{
                    validatePaymentDataDetail();
                    MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.PaymentProcess), MsgUtils.ToastLength.SHORT);

                    updateInvoicesDetailPayedAmount();
                    sendPaymentToDraft();
                    //((MainActivity) getActivity()).onAccountSelected();

                }catch (Exception ex){
                    MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, ex.getMessage(), MsgUtils.ToastLength.LONG);
                    ex.printStackTrace();
                }
            }
        });

        paymentCancelButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                try{

                    ((MainActivity)getActivity()).ClearPaymentData();
                    ((MainActivity)getActivity()).onAccountSelected();

                    String tmpPath = getContext().getExternalFilesDir(null).toString()+"/pdfFileTest.pdf";
                    ConverterProperties converterProperties = new ConverterProperties();
                    PdfDocument pdf = new PdfDocument(new PdfWriter(tmpPath));
                    pdf.setDefaultPageSize(new PageSize(210, 550));
                    HtmlConverter.convertToPdf("<html><body>M2 PDF TEST</body></html>",
                                                pdf,
                                                converterProperties);
                }catch (Exception ex){
                    MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, ex.getMessage(), MsgUtils.ToastLength.LONG);
                    ex.printStackTrace();
                }
            }
        });

        //Print Payment receipt
        paymentPrintButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                User user = getActiveUser();

                //Variable respuesta para tomar mensaje de funcion de Imprimir
                String respuesta ="";

                ArrayList<InvoiceItem> invoices = ((MainActivity) getActivity()).getInvoiceItems();
                Client client = new Client();

                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                String PaymentId = "";

                PaymentId =  String.valueOf(payment.getId());

                try
                {

                    if(user != null && client != null && invoices != null) {
                        respuesta = BluetoothPrinterPayment.print(context,
                                user.getPrintBluetoothAddress(),
                                date,
                                client,
                                user.getName(),
                                invoices,
                                0.0,
                                0.0,
                                payment.getComment(),
                                PaymentId,
                                payment.getPayedAmount());
                    }else
                    {
                        //MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, context.getString(R.string.Internal_error), MsgUtils.ToastLength.SHORT);
                        Toast.makeText(getActivity(), context.getString(R.string.Internal_error), Toast.LENGTH_LONG).show();
                    }

                    if ( respuesta != "")
                    {
                        //MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, respuesta, MsgUtils.ToastLength.LONG);
                        Toast.makeText(getActivity(), respuesta, Toast.LENGTH_LONG).show();
                    }


                }catch (Exception e){
                    //MsgUtils.showToast((Activity)context, MsgUtils.TOAST_TYPE_MESSAGE, e.getMessage().toString(), MsgUtils.ToastLength.LONG);
                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();

                };

            }
        });

        Bundle arguments = getArguments();
        if(arguments != null){
            payment = (Payment) arguments.getSerializable(ARG_PAYMENT);
            if(payment != null){
                ((MainActivity) getActivity()).ClearPaymentData();
                //((MainActivity) getActivity()).UpdateTransfer(payment.getTransfer());
                //((MainActivity) getActivity()).UpdateCash(payment.getCash());
                //((MainActivity) getActivity()).UpdateChecks(payment.getChecks());
                //((MainActivity) getActivity()).UpdateInvoiceItems(payment.getInvoices());
               // getBanks();
            } else {
                client = (Client) arguments.getSerializable(ARG_CLIENT);
                //getBanks();
            }
        }

        paymentCancelButton.setVisibility(View.VISIBLE);
        paymentSaveButton.setVisibility(View.VISIBLE);
        paymentSentToSapButton.setVisibility(View.GONE);

        mSectionsPagerAdapter = new PaymentMainFragment.SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = view.findViewById(R.id.payment_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setContentVisible(CONST.VISIBLE.CONTENT);

        return view;
    }

    private void clearPaymentDataDetail(){
        ((MainActivity)getActivity()).ClearPaymentData();
    }

    private String getFilePath(){
        String path = getContext().getExternalFilesDir(null).toString();
        String str =  client.getCardCode() + " " + client.getName() + " " + currentDateToString()+".pdf";
        return path + "/" + str.toUpperCase();
    }

    private String getHtmlInvoicesDetail(){
        String htmlTemplate = "<tr class='service'> <td class='tableitem'> <p class='itemtext'>@DocNum</p> </td> <td class='tableitem'> <p class='itemtext'>@BalanceDue</p> </td> <td class='tableitem'> <p class='itemtext'>@PayedAmount</p> </td> </tr>";
        ArrayList<InvoiceItem> invoices = ((MainActivity) getActivity()).getInvoiceItems();

        Locale _locale = ((MainActivity) getActivity()).getPaymentType()==3 ? new Locale("en", "US"):new Locale("es", "HN");
        String str = "";
        for (InvoiceItem item : invoices) {
            str = str + htmlTemplate.replace("@DocNum",item.documentNumber)
                                    .replace("@BalanceDue", NumberFormat.getCurrencyInstance(new Locale("es", "HN")).format(item.balanceDue))
                                    .replace("@PayedAmount", NumberFormat.getCurrencyInstance(_locale).format(item.payedAmount));
        }

        return str;
    }

    private void updateInvoicesDetailPayedAmount(){
        Double amountLeft = ((MainActivity) getActivity()).GetAmount();
        ArrayList<InvoiceItem> invoices = ((MainActivity) getActivity()).getInvoiceItems();

        if (invoices != null){

            Collections.sort(invoices, new SortByOverDueDays());

            for (InvoiceItem item : invoices) {
                if(amountLeft > 0){
                    item.payedAmount = item.balanceDue < amountLeft ? item.balanceDue : amountLeft;
                    amountLeft = amountLeft - item.payedAmount;
                }
            }
            ((MainActivity) getActivity()).UpdateInvoiceItems(invoices);
        }
    }
    private void createFilePDF(AddPaymentResponse paymentResponse){

        try{
            Locale _locale =  ((MainActivity) getActivity()).getPaymentType() ==3 ?new Locale("en","US"):new Locale("es","HN");

            String documentTitle = "Recibo de Pago";
            String receipt = String.valueOf(paymentResponse.getPaymentId());
            String payedDate = dateToString(((MainActivity) getActivity()).getDate(),"dd-MM-yyyy");
            String rDate = dateToString(new Date(),"dd-MM-yyyy HH:mm:ss");
            String _client = client.getCardCode() + " " + client.getName();
            String cRTN = client.getRtn();
            String paymentType = paymentTypes[((MainActivity) getActivity()).getPaymentType()];
            String referenceNumber = ((MainActivity) getActivity()).getReferenceNumber();
            String userName = SettingsMy.getActiveUser().getName();
            String totalAmount = NumberFormat.getCurrencyInstance(_locale).format(((MainActivity) getActivity()).GetAmount());
            String bank = ((MainActivity) getActivity()).getBank().getName();
            String note = "";

            if(paymentType.equals("Cheque")){
                documentTitle = "Recibo Provisional Por Cheque Pos Fechado";
                note = "<div id='legalcopy'><p class='legal'><strong>Nota:</strong> Este recibo es de caracter provisional y sera reemplazado por su recibo de pago al momento que su cheque sea cobrado.</p></div>";
            }

            String htmlTemplate = "<html lang='en'><head> <title> @DocumentTitle </title> <style> #invoice-POS { box-shadow: 0 0 1in -0.25in rgba(0, 0, 0, 0.5); padding: 2mm; margin: 0 auto; width: 44mm; background: #FFF;background-image: url('KD1.png');background-repeat: repeat-y;background-position: 50% 50%;background-size: 90px 40px; background-color: rgba(255,255,255,0.9); background-blend-mode: lighten;} #invoice-POS::selection { background: #f31544; color: #FFF; } #invoice-POS::moz-selection { background: #f31544; color: #FFF; } #invoice-POS h1 { font-size: 1.5em; color: #222; } #invoice-POS h2 { font-size: .9em; } #invoice-POS h3 { font-size: 1.2em; font-weight: 300; line-height: 2em; } #invoice-POS p { font-size: .7em; color: #666; line-height: 1.2em; } #invoice-POS #top, #invoice-POS #mid, #invoice-POS #bot { /* Targets all id with 'col-' */ border-bottom: 1px solid #EEE; } #invoice-POS #top { min-height: 10px; } #invoice-POS #mid { min-height: 80px; } #invoice-POS #bot { min-height: 50px; } #invoice-POS #top .logo { height: 60px; width: 60px; background: url(http://michaeltruong.ca/images/logo1.png) no-repeat; background-size: 60px 60px; } #invoice-POS .clientlogo { float: left; height: 60px; width: 60px; background: url(http://michaeltruong.ca/images/client.jpg) no-repeat; background-size: 60px 60px; border-radius: 50px; } #invoice-POS .info { display: block; margin-left: 0; } #invoice-POS .title { float: right; } #invoice-POS .title p { text-align: right; } #invoice-POS table { width: 100%; border-collapse: collapse; } #invoice-POS .tabletitle { font-size: .5em; background: #EEE; } #invoice-POS .service { border-bottom: 1px solid #EEE; } #invoice-POS .item { width: 17mm; } #invoice-POS .itemtext { font-size: .5em; } #invoice-POS #legalcopy { margin-top: 5mm; } </style> <script> window.console = window.console || function(t) {}; </script> <script> if (document.location.search.match(/type=embed/gi)) { window.parent.postMessage('resize', '*'); } </script></head><body translate='no'> <div id='invoice-POS' > <center id='top'> <!--<div class='logo'></div>--> <div class='info'> <h3>@DocumentTitle</h3> </div> </center> <div id='mid'> <div class='info'> <h2>VAN HEUSEN DE C.A.</h2> <p> Dirección : Col. San Fernando Ave. Juan Pablo II, frente a la Leyde. Apartado Postal #1. San Pedro Sula, Honduras, C.A. <br> Correo : vheusen@kattangroup.com <br> Teléfono : (504)2516-0100<br> Fax : (504)2516-4080<br> R.T.N. : 05019995143200 <br> </p> </div> <div class='info'> <h2>Recibo # @Receipt</h2> <p> Fecha de Recibo : @RDate <br> Cliente : @Client <br> R.T.N. : @CRTN <br> </p> </div> </div> <div id='bot'> <div id='table'> <table> <tbody> <tr class='tabletitle'> <td class='item'> <h2>Factura</h2></td> <td class='Hours'> <h2>Monto Pendiente</h2></td> <td class='Rate'> <h2>Monto Aplicado</h2></td> </tr>@PaymentItems <tr class='tabletitle'> <td></td> <td class='Rate'> <h2>Total</h2></td> <td class='payment'> <h2>@TotalAmount</h2></td> </tr> </tbody> </table> </div> <div id='legalcopy'> <p class='legal'>Forma de Pago : @PaymentType <br> No. de Referencia : @ReferenceNumber <br> Banco : @Bank <br> Fecha de Pago : @PayedDate <br> Vendedor : @UserName<br><br><strong>¡ Gracias por su pago !</strong>&nbsp; </p>@Note</div> </div> </div></body></html>";

            htmlTemplate = htmlTemplate.replace("@DocumentTitle",documentTitle)
                                       .replace("@Receipt",receipt)
                                       .replace("@PayedDate",payedDate)
                                       .replace("@RDate",rDate)
                                       .replace("@Client",_client)
                                       .replace("@CRTN",cRTN)
                                       .replace("@PaymentType",paymentType)
                                       .replace("@ReferenceNumber",referenceNumber)
                                       .replace("@UserName",userName)
                                       .replace("@TotalAmount",totalAmount)
                                       .replace("@Bank",bank)
                                       .replace("@PaymentItems",getHtmlInvoicesDetail())
                                       .replace("@Note",note);

            String tmpPath = getContext().getExternalFilesDir(null).toString()+"/pdfFileWithoutWaterMark.pdf";

            ConverterProperties converterProperties = new ConverterProperties();

            PdfDocument pdf = new PdfDocument(new PdfWriter(tmpPath));
            pdf.setDefaultPageSize(new PageSize(210, 550));


            HtmlConverter.convertToPdf(htmlTemplate,pdf,converterProperties);

            String path = getFilePath();

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(tmpPath), new PdfWriter(path));
            com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdfDoc);
            int n = pdfDoc.getNumberOfPages();

            // image watermark
            Drawable d = getContext().getDrawable(R.drawable.kadlogo);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            ImageData img = ImageDataFactory.create(bitmapdata);
            //  Implement transformation matrix usage in order to scale image
            float w = img.getWidth();
            float h = img.getHeight();
            // transparency
            PdfExtGState gs1 = new PdfExtGState();
            gs1.setFillOpacity(0.1f);
            // properties
            PdfCanvas over;
            Rectangle pagesize;
            float x, y;
            // loop over every page
            for (int i = 1; i <= n; i++) {
                PdfPage pdfPage = pdfDoc.getPage(i);
                pagesize = pdfPage.getPageSizeWithRotation();
                pdfPage.setIgnorePageRotationForContent(true);
                x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                over = new PdfCanvas(pdfDoc.getPage(i));
                over.saveState();
                over.setExtGState(gs1);
                over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2), false);
                over.restoreState();
            }
            doc.close();

        }catch (Exception ex){
            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, ex.getMessage(), MsgUtils.ToastLength.LONG);
            ex.printStackTrace();
        }

    }

    private void validatePaymentDataDetail() throws IOException{

        String comment = ((MainActivity) getActivity()).getComment();
        String reference = ((MainActivity) getActivity()).getReferenceNumber();
        double amount = ((MainActivity) getActivity()).GetAmount();
        int paymentType = ((MainActivity) getActivity()).getPaymentType();
        Bank bank = ((MainActivity) getActivity()).getBank();
        Date paymentDate = ((MainActivity) getActivity()).getDate();
        ArrayList<InvoiceItem> invoices = ((MainActivity) getActivity()).getInvoiceItems();
        double invoicesTotalAmount = ((MainActivity) getActivity()).sumImvoices();

        if(comment.isEmpty()){
            throw new IOException(getString(R.string.PaymentValidateComment));
        }

        if(reference.isEmpty()){
            throw new IOException(getString(R.string.PaymentValidateReference));
        }

        if(amount <= 0.00){
            throw new IOException(getString(R.string.PaymentValidateAmount));
        }

        if(paymentType < 0 || paymentType > 4){
            throw new IOException(getString(R.string.PaymentValidateType));
        }

        if(bank == null){
            throw new IOException(getString(R.string.PaymentValidateBank));
        }

        if(paymentDate == null){
            throw new IOException(getString(R.string.PaymentValidateDate));
        }

        if(invoices == null || invoices.size() == 0){
            throw new IOException(getString(R.string.PaymentValidateInvoice));
        }

        if(amount > invoicesTotalAmount){
            throw new IOException(getString(R.string.PaymentValidateInvoiceTotal));
        }
    }

    public void sendPaymentToDraft() {

        final User user = SettingsMy.getActiveUser();
        final Tenant tenant = SettingsMy.getActualTenant();
        if (user == null && tenant == null)
            return;

        JSONObject joPayment = new JSONObject();
        JSONArray joInvoices = new JSONArray();

        int paymentType = ((MainActivity) getActivity()).getPaymentType();
        String comment = "M2|" + paymentTypes[paymentType] + "|" + ((MainActivity) getActivity()).getComment();
        String referenceNumber = ((MainActivity) getActivity()).getReferenceNumber();
        double payedAmount = ((MainActivity) getActivity()).GetAmount();
        Bank bank =  ((MainActivity) getActivity()).getBank();
        Date payedDate = ((MainActivity) getActivity()).getDate() == null ?  new Date() : ((MainActivity) getActivity()).getDate();
        ArrayList<InvoiceItem> invoices = ((MainActivity) getActivity()).getInvoiceItems();

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        try {
            joPayment.put(JsonUtils.TAG_TENANT_ID, tenant.getId());
            joPayment.put(JsonUtils.TAG_PAYED_AMOUNT_ID, payedAmount);
            joPayment.put(JsonUtils.TAG_PAYED_STATUS, 0);
            joPayment.put(JsonUtils.TAG_COMMENT, comment);
            joPayment.put(JsonUtils.TAG_REFERENCE_NUMBER, referenceNumber);
            joPayment.put(JsonUtils.TAG_USER_ID, user.getId());
            joPayment.put(JsonUtils.TAG_BANK_ID, bank.getId());
            joPayment.put(JsonUtils.TAG_PAYMENT_TYPE, paymentType);
            joPayment.put(JsonUtils.TAG_PAYED_DATE, sdf.format(payedDate));
            joPayment.put(JsonUtils.TAG_CARD_CODE, client.getCardCode());

            if(invoices != null) {
                for (InvoiceItem invoice : invoices) {
                    JSONObject invoiceJSON = new JSONObject();
                    invoiceJSON.put(JsonUtils.TAG_DOCUMENT_CODE, invoice.getDocumentNumber());
                    invoiceJSON.put(JsonUtils.TAG_TOTAL_AMOUNT, invoice.getTotalAmount());
                    invoiceJSON.put(JsonUtils.TAG_BALANCE_DUE, invoice.getBalanceDue());
                    invoiceJSON.put(JsonUtils.TAG_PAYED_AMOUNT_ID, invoice.getPayedAmount());
                    invoiceJSON.put(JsonUtils.TAG_DOC_ENTRY, invoice.getDocEntry());
                    joInvoices.put(invoiceJSON);
                }
                joPayment.put(JsonUtils.TAG_PAYMENT_ITEM_LIST, joInvoices);
            }
        } catch (JSONException e) {
            String message = "Parse new transfer exception.";
            Timber.e(e, message);
            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, message, MsgUtils.ToastLength.SHORT);
        }

        //setContentVisible(CONST.VISIBLE.PROGRESS);
        GsonRequest<AddPaymentResult> req = new GsonRequest<>(Request.Method.POST, EndPoints.ADD_PAYMENT, joPayment.toString(), AddPaymentResult.class,
                new Response.Listener<AddPaymentResult>() {
                    @Override
                    public void onResponse(@NonNull AddPaymentResult response) {
                        //progressDialog.cancel();
                        Timber.d("Esto devolvio %s", response);
                        MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Success), MsgUtils.ToastLength.LONG);
                        createFilePDF(response.result);
                        clearPaymentDataDetail();
                        setContentVisible(CONST.VISIBLE.CONTENT);
                        ((MainActivity) getActivity()).onOpenClientFragment();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);

                setContentVisible(CONST.VISIBLE.CONTENT);
            }
        }, getFragmentManager(), user.getAccessToken());
        req.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        req.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(req, CONST.SENT_PAYMENT_TAG);
    }

    public void sendPaymentToSap()
    {

    }

    public void setFragments(View view){

        mSectionsPagerAdapter = new PaymentMainFragment.SectionsPagerAdapter(getFragmentManager());
        mViewPager = view.findViewById(R.id.payment_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    public void cancelPayment(int id)
    {
        User user = SettingsMy.getActiveUser();

        if(user == null)
            return;

        String url = String.format(EndPoints.PAYMENT_DELETE, id);

        GsonRequest<JSONObject> req = new GsonRequest<>(Request.Method.DELETE, url, null, JSONObject.class,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject payment) {
                        Timber.d("Esto devolvio %s", payment);
                        ((MainActivity) getActivity()).ClearPaymentData();
                        MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Success), MsgUtils.ToastLength.SHORT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        }, getFragmentManager(), user.getAccessToken());
        req.setRetryPolicy(MyApplication.getSimpleRetryPolice());
        req.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(req, CONST.CANCEL_PAYMENT_TAG);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int count = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if(position == 0)
                fragment = PaymentInvoiceFragment.newInstance(client);

            if(position == 1)
                fragment = PaymentTransferFragment.newInstance(client);

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";

            switch (position){
                case 0:
                    title = getString(R.string.PaymentPageTitleInvoice);
                    break;
                case 1:
                    title = getString(R.string.PaymentPageTitleData);
                    break;
            }

            return title;
        }
    }
}
