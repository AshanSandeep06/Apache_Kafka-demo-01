package lk.epic.kafka.consumer;

import lk.epic.kafka.ISO8583Message.ISO8583Message;
import lk.epic.kafka.producer.ISO8583MessageProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class ISO8583MessageConsumer {
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    ISO87APackager packager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ISO8583MessageConsumer.class);

    @KafkaListener(topics = "isoTopic", groupId = "myGroup")
    public void consumeISO8583Messages(byte[] consumerMsg) {
        try {
            ISOMsg isoMessage = new ISOMsg();
            isoMessage.setPackager(packager);
            isoMessage.unpack(consumerMsg);

            LOGGER.info(String.format("ISO8583 Message was Consumed by Consumer -> %s", ISOUtil.hexString(isoMessage.pack())));

            //-------------------------------------------------------

            ISOMsg iso8583Response = ISO8583Message.getInstance().getIsoMessage();
            iso8583Response.setMTI("0110");
            iso8583Response.set("3", "000000");
            iso8583Response.set("4", "000000430000");
            iso8583Response.set("11", "001271");
            iso8583Response.set("22", "052");
            iso8583Response.set("24", "875");
            iso8583Response.set("25", "00");

            iso8583Response.set(35, "7A5A\u0012e\t\u0083Ò`R\u0001\u0007\u0097yp\u0010");
            iso8583Response.set(41, "40203344");
            iso8583Response.set(42, "000000009913000");
            iso8583Response.set(55, "\u0001A\u009F'\u0001\u0080\u009F\u0010\u0007\u0006\u0001\n\u0003  \u0002\u009F7\u0004$wRð\u009F6\u0002\u00018\u0095\u0005\u0080\u0080\u009A\u0003#\u0004\t\u009C\u0001\u009F\u0002\u0006C_*\u0002\u0001D\u0082\u0002<\u009F\u001A\u0002\u0001D\u009F\u0003\u0006\u009F3\u0003à¸È\u009F4\u0003\u001E\u0003\u009F5\u0001\"\u009F\u001E\b04702988\u0084\u0007 \u0003\u0010\u0010\u009F\t\u0002\u008C_4\u0001\u0001\u009B\u0002è\u009F@\u0005ÿ\u0080ð \u0001\u009F&\b®\u0094oP\u0018FT\t");
            iso8583Response.set(62, "\u0006001271");

            iso8583Response.setPackager(packager);
            byte[] packedData = iso8583Response.pack();

            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

            // Create a Kafka producer instance
            Producer<String, byte[]> producer = new KafkaProducer<>(properties);

            // Specify the partition and create a ProducerRecord
            String topic = "isoTopic";
            int partition = 1;  // Replace with the desired partition number
            String key = "response";
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, partition, key, packedData);

            // Send the message
            producer.send(record);

            // Close the producer
            producer.close();

            // ---------------------------------------------------------------------------------------

//            ISOMsg iso8583Response = ISO8583Message.getInstance().getIsoMessage();
//            iso8583Response.setMTI("0110");
//            iso8583Response.set("3", "000000");
//            iso8583Response.set("4", "000000430000");
//            iso8583Response.set("11", "001271");
//            iso8583Response.set("22", "052");
//            iso8583Response.set("24", "875");
//            iso8583Response.set("25", "00");
//
//            iso8583Response.set(35, "7A5A\u0012e\t\u0083Ò`R\u0001\u0007\u0097yp\u0010");
//            iso8583Response.set(41, "40203344");
//            iso8583Response.set(42, "000000009913000");
//            iso8583Response.set(55, "\u0001A\u009F'\u0001\u0080\u009F\u0010\u0007\u0006\u0001\n\u0003  \u0002\u009F7\u0004$wRð\u009F6\u0002\u00018\u0095\u0005\u0080\u0080\u009A\u0003#\u0004\t\u009C\u0001\u009F\u0002\u0006C_*\u0002\u0001D\u0082\u0002<\u009F\u001A\u0002\u0001D\u009F\u0003\u0006\u009F3\u0003à¸È\u009F4\u0003\u001E\u0003\u009F5\u0001\"\u009F\u001E\b04702988\u0084\u0007 \u0003\u0010\u0010\u009F\t\u0002\u008C_4\u0001\u0001\u009B\u0002è\u009F@\u0005ÿ\u0080ð \u0001\u009F&\b®\u0094oP\u0018FT\t");
//            iso8583Response.set(62, "\u0006001271");
//
//            iso8583Response.setPackager(packager);
//            byte[] packedData = iso8583Response.pack();
//
//            Message<byte[]> finalMessage = MessageBuilder.withPayload(packedData)
//                    .setHeader(KafkaHeaders.TOPIC, "isoTopic").build();
//
//            LOGGER.info(String.format("Message Sent From Consumer to Topic -> %s", finalMessage.toString()));
//
//            kafkaTemplate.send(finalMessage);

//            ISOMsg iso8583Response = ISO8583Message.getInstance().getIsoMessage();
//            iso8583Response.setMTI("0110");
//            iso8583Response.set("3", "000000");
//            iso8583Response.set("4", "000000430000");
//            iso8583Response.set("11", "001271");
//            iso8583Response.set("22", "052");
//            iso8583Response.set("24", "875");
//            iso8583Response.set("25", "00");
//
//            iso8583Response.set(35, "7A5A\u0012e\t\u0083Ò`R\u0001\u0007\u0097yp\u0010");
//            iso8583Response.set(41, "40203344");
//            iso8583Response.set(42, "000000009913000");
//            iso8583Response.set(55, "\u0001A\u009F'\u0001\u0080\u009F\u0010\u0007\u0006\u0001\n\u0003  \u0002\u009F7\u0004$wRð\u009F6\u0002\u00018\u0095\u0005\u0080\u0080\u009A\u0003#\u0004\t\u009C\u0001\u009F\u0002\u0006C_*\u0002\u0001D\u0082\u0002<\u009F\u001A\u0002\u0001D\u009F\u0003\u0006\u009F3\u0003à¸È\u009F4\u0003\u001E\u0003\u009F5\u0001\"\u009F\u001E\b04702988\u0084\u0007 \u0003\u0010\u0010\u009F\t\u0002\u008C_4\u0001\u0001\u009B\u0002è\u009F@\u0005ÿ\u0080ð \u0001\u009F&\b®\u0094oP\u0018FT\t");
//            iso8583Response.set(62, "\u0006001271");
//
//            iso8583Response.setPackager(packager);
//            byte[] packedData = iso8583Response.pack();
//
//            LOGGER.info(String.format("Message Sent From Consumer to Topic -> %s", ISOUtil.hexString(isoMessage.pack())));
//
//            // Send the response to the producer
//            producer.send(new ProducerRecord<>("isoTopic", "response", packedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
