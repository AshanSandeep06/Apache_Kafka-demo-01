package lk.epic.kafka.ISO8583Message;

import org.jpos.iso.ISOMsg;

public class ISO8583Message {
    private static ISO8583Message iso8583Message;
    private final ISOMsg isoMessage;

    private ISO8583Message() {
        isoMessage = new ISOMsg();
    }

    public static ISO8583Message getInstance() {
        return iso8583Message == null ? iso8583Message = new ISO8583Message() : iso8583Message;
    }

    public ISOMsg getIsoMessage() {
        return isoMessage;
    }
}
