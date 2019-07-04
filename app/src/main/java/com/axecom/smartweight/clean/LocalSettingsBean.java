//package com.axecom.smartweight.bean;
//
//import com.axecom.smartweight.base.BaseEntity;
//
//import java.io.Serializable;
//import java.util.List;
//
//public class LocalSettingsBean<T> extends BaseEntity implements Serializable{
//    public Value value;
//
//    public List<CardReaderTypeList> card_reader_type_list;
//    public List<WeightPort> weight_port;
//    public List<PrinterPort> printer_port;
//    public List<ExternalLedPort> external_led_port;
//    public List<CardReaderPort> card_reader_port;
//
//    public class Value implements Serializable {
//        public NumberOfPrintsConfiguration number_of_prints_configuration;
//        public LotValidityTime lot_validity_time;
//        public ClearTransactionData clear_transaction_data;
//        public ScreenOff screen_off;
//        public WeighingPlateBaudRate weighing_plate_baud_rate;
//        public CardReaderType card_reader_type;
//        public ServerIp server_ip;
//        public ServerPort server_port;
//        public PrinterPort printer_port;
//        public WeightPort weight_port;
//        public CardReaderPort card_reader_port;
//
//
//        public class NumberOfPrintsConfiguration implements Serializable {
//            public String val;
//            public String update_time;
//        }
//
//        public class LotValidityTime implements Serializable {
//            public String val;
//            public String update_time;
//        }
//        public class WeightPort implements Serializable {
//            public String val;
//            public String update_time;
//        }
//        public class PrinterPort implements Serializable {
//            public String val;
//            public String update_time;
//        }
//
//        public class ClearTransactionData  implements Serializable{
//            public String val;
//            public String update_time;
//        }
//
//        public class ScreenOff implements Serializable {
//            public String val;
//            public String update_time;
//        }
//
//        public class WeighingPlateBaudRate implements Serializable {
//            public String val;
//            public String update_time;
//        }
//
//        public class CardReaderType implements Serializable {
//            public String val;
//            public String update_time;
//        }
//
//        public class CardReaderPort implements Serializable {
//            public String val;
//            public String update_time;
//        }
//
//
//        public class ServerIp  implements Serializable{
//            public String val;
//            public String update_time;
//        }
//
//        public class ServerPort implements Serializable {
//            public String val;
//            public String update_time;
//        }
//    }
//
//    public class CardReaderTypeList {
//        public String val;
//    }
//
//    public class WeightPort {
//        public String val;
//    }
//
//    public class PrinterPort {
//        public String val;
//    }
//
//    public class ExternalLedPort {
//        public String val;
//    }
//
//    public class CardReaderPort {
//        public String val;
//    }
//
//
//}
