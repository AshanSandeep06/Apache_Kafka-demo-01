package lk.epic.kafka.producer;

import lk.epic.kafka.ISO8583Message.ISO8583Message;
import lk.epic.kafka.dto.ISO8583FieldsDTO;
import lk.epic.kafka.entity.ISO8583Fields;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ISO8583MessageProducer {
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    ISO87APackager packager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ISO8583MessageProducer.class);

    public void sendISO8583Message(ISO8583FieldsDTO allFields) {
        try {
            ISOMsg message = ISO8583Message.getInstance().getIsoMessage();
            message.setMTI(allFields.getF0());
            message.set("3", allFields.getF3());
            message.set("4", allFields.getF4());
            message.set("11", allFields.getF11());
            message.set("22", allFields.getF22());
            message.set("24", allFields.getF24());
            message.set("25", allFields.getF25());

            message.set(35, allFields.getF35());
            message.set(41, allFields.getF41());
            message.set(42, allFields.getF42());
            message.set(55, allFields.getF55());
            message.set(62, allFields.getF62());

            message.setPackager(packager);
            byte[] packedData = message.pack();

            Message<byte[]> finalMessage = MessageBuilder.withPayload(packedData)
                    .setHeader(KafkaHeaders.TOPIC, "isoTopic").build();

            LOGGER.info(String.format("Message Sent From Producer to Topic -> %s", finalMessage.toString()));

            kafkaTemplate.send(finalMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "isoTopic", groupId = "myGroup")
    public void consumeISO8583Messages(byte[] replyMessage) {
        try {
            ISOMsg iso8583Response = ISO8583Message.getInstance().getIsoMessage();
            iso8583Response.setPackager(packager);
            iso8583Response.unpack(replyMessage);

            LOGGER.info(String.format("ISO8583 Message was Received to Producer -> %s", ISOUtil.hexString(iso8583Response.pack())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
