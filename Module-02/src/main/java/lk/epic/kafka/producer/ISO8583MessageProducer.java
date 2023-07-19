package lk.epic.kafka.producer;

import lk.epic.kafka.ISO8583Message.ISO8583Message;
import lk.epic.kafka.dto.ISO8583FieldsDTO;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jpos.iso.ISOException;
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
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Arrays;

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

            // Send the message to Kafka and get the future result
            /*ListenableFuture<SendResult<String, byte[]>> future = kafkaTemplate.send(finalMessage);
            LOGGER.info("Received response from Kafka producer: ");
            System.out.println("------------------------------------" + future.get().getProducerRecord());
            System.out.println("------------------------------------" + future.completable().get().getProducerRecord().value());
            System.out.println("------------------------------------" + future.completable().get().getRecordMetadata().toString().getBytes());
            System.out.println("------------------------------------" + future.get().toString().getBytes().toString());*/

            // Attach a callback to the future result
            /*future.addCallback(new ListenableFutureCallback<SendResult<String, byte[]>>() {
                @Override
                public void onSuccess(SendResult<String, byte[]> result) {
                    *//*ISOMsg iso8583Response = new ISOMsg();
                    iso8583Response.setPackager(packager);
                    try {
                        iso8583Response.unpack(result.getProducerRecord().value());
                        LOGGER.info(String.format("Message Sent From Producer to Topic -> %s", ISOUtil.hexString(iso8583Response.pack())));
                    } catch (ISOException e) {
                        e.printStackTrace();
                    }*//*

                    // -----------------------------------------

                    // Handle successful message sending
                    *//*RecordMetadata metadata = result.getRecordMetadata();
                    System.out.println();
                    LOGGER.info("Received response from Kafka producer: " + Arrays.toString(metadata.toString().getBytes()));*//*

                    // Process the response received from the consumer
                    ISOMsg iso8583Response = new ISOMsg();
                    iso8583Response.setPackager(packager);
                    try {

                        System.out.println("On success : " + result.toString());
                        System.out.println("On success : " + result.getProducerRecord());
                        System.out.println("On success : " + result.getProducerRecord().value());
                        System.out.println("On success : " + result.getProducerRecord().toString());
                        System.out.println("On success : " + result.getRecordMetadata());
                        System.out.println("On success : " + result.getRecordMetadata().toString());
                        System.out.println("On success : " + result.getRecordMetadata().topic());

                        iso8583Response.unpack(result.getProducerRecord().value());
                        LOGGER.info("Received ISO8583 response from consumer: " + ISOUtil.hexString(iso8583Response.pack()));

                        // Process the response as needed
                    } catch (ISOException e) {
                        LOGGER.error("Error unpacking the ISO8583 response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.error("Failed to send response message: " + ex.getMessage());
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "isoResponseTopic", groupId = "myGroup")
    public void consumeISO8583Response(byte[] responseMessage) {
        try {
            ISOMsg iso8583Response = new ISOMsg();
            iso8583Response.setPackager(packager);
            iso8583Response.unpack(responseMessage);

            LOGGER.info(String.format("Received ISO8583 response from consumer -> %s", ISOUtil.hexString(iso8583Response.pack())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
