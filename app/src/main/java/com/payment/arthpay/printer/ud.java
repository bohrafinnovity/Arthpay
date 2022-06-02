package com.payment.arthpay.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintDocumentInfo.Builder;
import android.print.pdf.PrintedPdfDocument;

import androidx.annotation.RequiresApi;

import com.payment.arthpay.R;

import org.egram.aepslib.apiService.Body.PrintDataModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

@RequiresApi(
    api = 19
)
public class ud extends PrintDocumentAdapter {
    public Context context;
    public int bb;
    public int db;
    public PdfDocument eb;
    public int fb = 1;
    public ArrayList<PrintDataModel> gb;
    public String name;
    public String hb;

    public ud(Context var1, ArrayList<PrintDataModel> var2, String var3, String var4) {
        this.context = var1;
        this.gb = var2;
        this.name = var3;
        this.hb = var4;
    }

    public void onLayout(PrintAttributes var1, PrintAttributes var2, CancellationSignal var3, LayoutResultCallback var4, Bundle var5) {
        this.eb = new PrintedPdfDocument(this.context, var2);
        this.bb = var2.getMediaSize().getHeightMils() / 1000 * 72;
        this.db = var2.getMediaSize().getWidthMils() / 1000 * 72;
        if (var3.isCanceled()) {
            var4.onLayoutCancelled();
        } else {
            if (this.fb > 0) {
                var4.onLayoutFinished((new Builder("print_receipt.pdf")).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(this.fb).build(), true);
            } else {
                var4.onLayoutFailed("Page count is zero.");
            }

        }
    }

    public void onWrite(PageRange[] var1, ParcelFileDescriptor var2, CancellationSignal var3, WriteResultCallback var4) {
        for(int var5 = 0; var5 < this.fb; ++var5) {
            if (this.NUL(var1, var5)) {
                PageInfo var6 = (new PageInfo.Builder(this.db, this.bb, var5)).create();
                Page var13 = this.eb.startPage(var6);
                if (var3.isCanceled()) {
                    var4.onWriteCancelled();
                    this.eb.close();
                    this.eb = null;
                    return;
                }

                this.NUL(var13, var5);
                this.eb.finishPage(var13);
            }
        }

        WriteResultCallback var12;
        PageRange[] var14;
        ud var10002;
        ud var10003;
        label62: {
            ud var10000;
            ud var10001;
            try {
                try {
                    var12 = var4;
                    var14 = var1;
                    var10002 = this;
                    var10003 = this;
                    this.eb.writeTo(new FileOutputStream(var2.getFileDescriptor()));
                    break label62;
                } catch (IOException var9) {
                    String var11 = var9.toString();
                    var10000 = this;
                    var10001 = this;
                    var4.onWriteFailed(var11);
                }
            } catch (Throwable var10) {
                this.eb.close();
                this.eb = null;
                throw var10;
            }

            var10001.eb.close();
            var10000.eb = null;
            return;
        }

        var10003.eb.close();
        var10002.eb = null;
        var12.onWriteFinished(var14);
    }

    public final boolean NUL(PageRange[] var1, int var2) {
        for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var2 >= var1[var3].getStart() && var2 <= var1[var3].getEnd()) {
                return true;
            }
        }

        return false;
    }

    public final void NUL(Page var1, int var2) {
        Canvas var9;
        Canvas var10001 = var9 = var1.getCanvas();
        byte var10 = 72;
        byte var3 = 70;
        Paint var4;
        Paint var10003 = var4 = new Paint();
        //var10003.<init>();
        var10003.setColor(-16777216);
        var10003.setTextSize(16.0F);
        Paint var5;
        var10003 = var5 = new Paint();
        //var10003.<init>();
        var10003.setColor(-16777216);
        var10003.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        var10003.setTextSize(17.0F);
        Paint var6;
        var10003 = var6 = new Paint();
        //var10003.<init>();
        var10003.setAntiAlias(true);
        var10003.setFilterBitmap(true);
        var10003.setDither(true);
        //var10001.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.context.getResources(), drawable), 170, 50, false), 65.0F, 50.0F, var6);
        if (!this.hb.isEmpty() && this.hb.equalsIgnoreCase("Equitas")) {
            //var9.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.context.getResources(), drawable.equitas_logo_big), 150, 50, false), 250.0F, 50.0F, var6);
        } else if (!this.hb.isEmpty() && this.hb.equalsIgnoreCase("Icici")) {
            //var9.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.context.getResources(), drawable.icici_logo), 150, 50, false), 250.0F, 50.0F, var6);
        } else if (!this.hb.isEmpty() && this.hb.equalsIgnoreCase("Kotak")) {
            //var9.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.context.getResources(), drawable.icon_kotak_bank), 150, 50, false), 250.0F, 50.0F, var6);
        }else if (!this.hb.isEmpty() && this.hb.equalsIgnoreCase("app_name")) {
            var9.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.app_logo), 220, 40, false), 60.0F, 50.0F, var6);
        }

        float var11 = (float)var3;
        float var14 = (float)112;
        var9.drawText("*******************************************************************************", var11, var14, var4);
//        var9.drawText(this.name, var11, (float)142, var5);
        float var12 = (float)172;
        //var9.drawText("-------------------------------------------------------------------------------", var11, var12, var4);
        int var13 = 70;
        Iterator var15 = this.gb.iterator();

        while(var15.hasNext()) {
            PrintDataModel var7 = (PrintDataModel)var15.next();
            int var16;
            if ((var16 = this.NUL(var9, var4, var11, (float)(var10 + var13), var7.getHead() + " : " + var7.getValue())) > 1) {
                var13 += var16 * 16 + 20;
            } else {
                var13 += 30;
            }
        }

        float var8 = (float)(var10 + var13 + 10);
        var9.drawText("*******************************************************************************", var11, var8, var4);
    }

    public final int NUL(Canvas var1, Paint var2, float var3, float var4, String var5) {
        float var9 = var2.descent() - var2.ascent();
        int var6 = 0;
        var5.length();

        int var10000;
        int var7;
        do {
            var7 = var5.length();
            char[] var10001 = var5.toCharArray();
            int var8;
            var10000 = var8 = var2.breakText(var10001, 0, var10001.length, 480.0F, (float[])null);
            String var10005 = var5.substring(0, var8);
            var5 = var5.substring(var8, var5.length());
            var1.drawText(var10005, var3, var4, var2);
            var4 += var9;
            ++var6;
        } while(var10000 < var7);

        return var6;
    }
}
