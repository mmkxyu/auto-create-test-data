/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: CsvWriterExtend
 * Author:   yml
 * Date:     2019/5/17 15:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 俞美玲           2019.5.17           1.0.0              优化CsvWriter工具
 */
package com.test.csv.tool;

import com.csvreader.CsvWriter;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @创建人 yumeiling
 * @创建时间 2019/5/17
 * @描述 优化CsvWriter工具，解决了写入csv打开后中文乱码问题，解决了写入csv最后一行有/r换行的问题
 *
 */
public class CsvWriterExtend extends CsvWriter {
    private PrintWriter outputStream;
    private String fileName;
    private boolean firstColumn;
    private boolean useCustomRecordDelimiter;
    private Charset charset;
    private CsvWriterExtend.UserSettings userSettings;
    private boolean initialized;
    private boolean closed;
    public static final int ESCAPE_MODE_DOUBLED = 1;
    public static final int ESCAPE_MODE_BACKSLASH = 2;
    private String sheetFirstName;

    public CsvWriterExtend(String var1, char var2, Charset var3,String sheetFirstName) {
        super(var1,var2,var3);
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new CsvWriterExtend.UserSettings();
        this.initialized = false;
        this.closed = false;
        this.sheetFirstName = sheetFirstName;
        if (var1 == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (var3 == null) {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        } else {
            this.fileName = var1;
            this.userSettings.Delimiter = var2;
            this.charset = var3;
        }
    }

    public CsvWriterExtend(Writer var1, char var2) {
        super(var1,var2);
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new CsvWriterExtend.UserSettings();
        this.initialized = false;
        this.closed = false;
        if (var1 == null) {
            throw new IllegalArgumentException("Parameter outputStream can not be null.");
        } else {
            this.outputStream = new PrintWriter(var1);
            this.userSettings.Delimiter = var2;
            this.initialized = true;
        }
    }

    public CsvWriterExtend(OutputStream var1, char var2, Charset var3) {
        this(new OutputStreamWriter(var1, var3), var2);
    }

    public char getDelimiter() {
        return this.userSettings.Delimiter;
    }

    public void setDelimiter(char var1) {
        this.userSettings.Delimiter = var1;
    }

    public char getRecordDelimiter() {
        return this.userSettings.RecordDelimiter;
    }

    public void setRecordDelimiter(char var1) {
        this.useCustomRecordDelimiter = true;
        this.userSettings.RecordDelimiter = var1;
    }

    public char getTextQualifier() {
        return this.userSettings.TextQualifier;
    }

    public void setTextQualifier(char var1) {
        this.userSettings.TextQualifier = var1;
    }

    public boolean getUseTextQualifier() {
        return this.userSettings.UseTextQualifier;
    }

    public void setUseTextQualifier(boolean var1) {
        this.userSettings.UseTextQualifier = var1;
    }

    public int getEscapeMode() {
        return this.userSettings.EscapeMode;
    }

    public void setEscapeMode(int var1) {
        this.userSettings.EscapeMode = var1;
    }

    public void setComment(char var1) {
        this.userSettings.Comment = var1;
    }

    public char getComment() {
        return this.userSettings.Comment;
    }

    public boolean getForceQualifier() {
        return this.userSettings.ForceQualifier;
    }

    public void setForceQualifier(boolean var1) {
        this.userSettings.ForceQualifier = var1;
    }

    public void write(String var1, boolean var2) throws IOException {
        this.checkClosed();
        this.checkInit();
        if (var1 == null) {
            var1 = "";
        }
        //如果是写入bom则bom后面不追加逗号，即在bom后面和第一个表头前面，即两者之间不追加逗号,sheetFirstName为第一个表头的名字根据需求传入
        if (!this.firstColumn && !var1.contentEquals(sheetFirstName) ){
            this.outputStream.write(this.userSettings.Delimiter);
        }

        boolean var3 = this.userSettings.ForceQualifier;
        if (!var2 && var1.length() > 0) {
            var1 = var1.trim();
        }

        if (!var3 && this.userSettings.UseTextQualifier && (var1.indexOf(this.userSettings.TextQualifier) > -1 || var1.indexOf(this.userSettings.Delimiter) > -1 || !this.useCustomRecordDelimiter && (var1.indexOf(10) > -1 || var1.indexOf(13) > -1) || this.useCustomRecordDelimiter && var1.indexOf(this.userSettings.RecordDelimiter) > -1 || this.firstColumn && var1.length() > 0 && var1.charAt(0) == this.userSettings.Comment || this.firstColumn && var1.length() == 0)) {
            var3 = true;
        }

        if (this.userSettings.UseTextQualifier && !var3 && var1.length() > 0 && var2) {
            char var4 = var1.charAt(0);
            if (var4 == ' ' || var4 == '\t') {
                var3 = true;
            }

            if (!var3 && var1.length() > 1) {
                char var5 = var1.charAt(var1.length() - 1);
                if (var5 == ' ' || var5 == '\t') {
                    var3 = true;
                }
            }
        }
        if(!this.firstColumn && var1.length()==0){
            var3=true;
        }
        if (var3) {
            this.outputStream.write(this.userSettings.TextQualifier);
            if (this.userSettings.EscapeMode == 2) {
                var1 = replace(var1, "\\", "\\\\");
                var1 = replace(var1, "" + this.userSettings.TextQualifier, "\\" + this.userSettings.TextQualifier);
            } else {
                var1 = replace(var1, "" + this.userSettings.TextQualifier, "" + this.userSettings.TextQualifier + this.userSettings.TextQualifier);
            }
        } else if (this.userSettings.EscapeMode == 2) {
            var1 = replace(var1, "\\", "\\\\");
            var1 = replace(var1, "" + this.userSettings.Delimiter, "\\" + this.userSettings.Delimiter);
            if (this.useCustomRecordDelimiter) {
                var1 = replace(var1, "" + this.userSettings.RecordDelimiter, "\\" + this.userSettings.RecordDelimiter);
            } else {
                var1 = replace(var1, "\r", "\\\r");
                var1 = replace(var1, "\n", "\\\n");
            }

            if (this.firstColumn && var1.length() > 0 && var1.charAt(0) == this.userSettings.Comment) {
                if (var1.length() > 1) {
                    var1 = "\\" + this.userSettings.Comment + var1.substring(1);
                } else {
                    var1 = "\\" + this.userSettings.Comment;
                }
            }
        }


        this.outputStream.write(var1);

        if (var3) {
            this.outputStream.write(this.userSettings.TextQualifier);
        }

        this.firstColumn = false;
    }

    public void write(String var1) throws IOException {
        this.write(var1, false);
    }

    public void writeComment(String var1) throws IOException {
        this.checkClosed();
        this.checkInit();
        this.outputStream.write(this.userSettings.Comment);
        this.outputStream.write(var1);
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.RecordDelimiter);
        } else {
            this.outputStream.println();
        }

        this.firstColumn = true;
    }

    public void writeRecord(String[] var1, boolean var2) throws IOException {
        if (var1 != null && var1.length > 0) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
                this.write(var1[var3], var2);
            }

            this.endRecord();
        }

    }

    public void writeRecord(String[] var1) throws IOException {
        this.writeRecord(var1, false);
    }

    public void writeLastRecord(String[] var1) throws IOException {
        this.writeLastRecord(var1, false);
    }
    public void writeLastRecord(String[] var1, boolean var2) throws IOException {
        if (var1 != null && var1.length > 0) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
                this.write(var1[var3], var2);
            }

            this.endLastRecord();
        }

    }
    public void endLastRecord() throws IOException {
        this.checkClosed();
        this.checkInit();
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.RecordDelimiter);
        } else {
            this.outputStream.print("");
        }

        this.firstColumn = true;
    }

    public void endRecord() throws IOException {
        this.checkClosed();
        this.checkInit();
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.RecordDelimiter);
        } else {
            this.outputStream.println();
        }

        this.firstColumn = true;
    }

    private void checkInit() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null) {
                this.outputStream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.fileName), this.charset));
            }

            this.initialized = true;
        }

    }

    public void flush() {
        this.outputStream.flush();
    }

    public void close() {
        if (!this.closed) {
            this.close(true);
            this.closed = true;
        }

    }

    private void close(boolean var1) {
        if (!this.closed) {
            if (var1) {
                this.charset = null;
            }

            try {
                if (this.initialized) {
                    this.outputStream.close();
                }
            } catch (Exception var3) {
                ;
            }

            this.outputStream = null;
            this.closed = true;
        }

    }

    private void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("This instance of the CsvWriter class has already been closed.");
        }
    }

    protected void finalize() {
        this.close(false);
    }

    public static String replace(String var0, String var1, String var2) {
        int var3 = var1.length();
        int var4 = var0.indexOf(var1);
        if (var4 <= -1) {
            return var0;
        } else {
            StringBuffer var5 = new StringBuffer();

            int var6;
            for(var6 = 0; var4 != -1; var4 = var0.indexOf(var1, var6)) {
                var5.append(var0.substring(var6, var4));
                var5.append(var2);
                var6 = var4 + var3;
            }

            var5.append(var0.substring(var6));
            return var5.toString();
        }
    }

    private class UserSettings {
        public char TextQualifier = '"';
        public boolean UseTextQualifier = true;
        public char Delimiter = ',';
        public char RecordDelimiter = 0;
        public char Comment = '#';
        public int EscapeMode = 1;
        public boolean ForceQualifier = false;

        public UserSettings() {
        }
    }

    private class Letters {
        public static final char LF = '\n';
        public static final char CR = '\r';
        public static final char QUOTE = '"';
        public static final char COMMA = ',';
        public static final char SPACE = ' ';
        public static final char TAB = '\t';
        public static final char POUND = '#';
        public static final char BACKSLASH = '\\';
        public static final char NULL = '\u0000';

        private Letters() {
        }
    }
}