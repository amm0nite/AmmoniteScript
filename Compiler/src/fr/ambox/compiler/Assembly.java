package fr.ambox.compiler;

import com.google.gson.Gson;
import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.MetaData;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembly {
    private String code;
    private MetaData meta;

    public Assembly(String code) {
        this.code = code;
        this.meta = null;

        String extract = code.substring(0, 8);
        Pattern pattern = Pattern.compile("META(\\d{4})");
        Matcher matcher = pattern.matcher(extract);
        boolean found = matcher.matches();
        if (found) {
            int metaLength = Integer.parseInt(matcher.group(1));
            String metaString = code.substring(8, 8 + metaLength);
            this.code = code.substring(8 + metaLength + 1);

            Gson gson = new Gson();
            this.meta = gson.fromJson(metaString, MetaData.class);
        }
    }

    public Assembly(AtomList atoms) {
        this(atoms.toCode());
    }

    public String toString() {
        Gson gson = new Gson();

        String metadata = gson.toJson(this.meta);
        String lengthLabel = String.valueOf(metadata.length());
        while (lengthLabel.length() < 4) {
            lengthLabel = "0" + lengthLabel;
        }
        String metaString = "META"+lengthLabel + metadata;
        return metaString + " " + this.code;
    }

    public void prepareMeta() {
        if (this.meta == null) {
            this.meta = new MetaData();
        }
        this.meta.date = (new Date()).toString();
    }

    public String getCode() {
        return this.code;
    }

    public MetaData getMeta() {
        return this.meta;
    }
}
