package com.lvqingyang.callenterprise.bean;

import java.util.List;

/**
 * Author：LvQingYang
 * Date：2017/9/1
 * Email：biloba12345@gamil.com
 * Github：https://github.com/biloba123
 * Info：
 */
public class Order {

    /**
     * CallTasks : [{"CallResult":{"RunningID":1000000001,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000001,"CallNo":"18696162662","Name":"何泽鹏","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000002,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000002,"CallNo":"13260520443","Name":"廖庆涵","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000003,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000003,"CallNo":"15660521487","Name":"王子心","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000004,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000004,"CallNo":"18660514756","Name":"周沁仪","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000005,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000005,"CallNo":"13260517852","Name":"王悦","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000006,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000006,"CallNo":"18658689618","Name":"刘总","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000007,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000007,"CallNo":"13071258688","Name":"彭科长","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000008,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000008,"CallNo":"13554018566","Name":"周润发","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000009,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000009,"CallNo":"18898186888","Name":"王健林","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000010,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000010,"CallNo":"13018186866","Name":"王思聪","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000011,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000011,"CallNo":"15656891234","Name":"杨幂","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000012,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000012,"CallNo":"18956661818","Name":"郭富城","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000013,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000013,"CallNo":"18696881678","Name":"周星驰","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000014,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000014,"CallNo":"15628583666","Name":"陈女士","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000015,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000015,"CallNo":"13548755822","Name":"张小姐","OrderID":1000028,"ServerID":null},{"CallResult":{"RunningID":1000000016,"ResultType":0,"Note":"无","AudioUrl":null},"CustomServer":null,"RunningID":1000000016,"CallNo":"13056123567","Name":"李先生","OrderID":1000028,"ServerID":null}]
     * OrderID : 1000028
     * EnterID : 1000009
     * CreateTime : 2017-08-06T14:34:38.94
     * Complete : false
     * PrePay : 0.64
     * FinalPay : 0.0
     * EndTime : 2017-09-06T14:34:38.94
     * OrderType : 2
     * State : 2
     * RefuseReson : null
     * UnitPrice : 0.1
     * PhoneCount : 16
     * CallListUrl : ~\App_Data\CallTaskList\2017080614340963电话列表20个.txt
     * OrderName : 通知客户8月15号到公司签协议
     * CallContent : 1.通知客户8月15号到公司签署最终成交协议
     * IsExpress : false
     */

    private int OrderID;
    private int EnterID;
    private String CreateTime;
    private boolean Complete;
    private double PrePay;
    private double FinalPay;
    private String EndTime;
    private int OrderType;
    private int State;
    private String RefuseReson;
    private double UnitPrice;
    private int PhoneCount;
    private String CallListUrl;
    private String OrderName;
    private String CallContent;
    private boolean IsExpress;
    private boolean SendSMS;
    private List<CallTasksBean> CallTasks;

    public boolean isExpress() {
        return IsExpress;
    }

    public void setExpress(boolean express) {
        IsExpress = express;
    }

    public boolean isSendSMS() {
        return SendSMS;
    }

    public void setSendSMS(boolean sendSMS) {
        SendSMS = sendSMS;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getEnterID() {
        return EnterID;
    }

    public void setEnterID(int EnterID) {
        this.EnterID = EnterID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public boolean isComplete() {
        return Complete;
    }

    public void setComplete(boolean Complete) {
        this.Complete = Complete;
    }

    public double getPrePay() {
        return PrePay;
    }

    public void setPrePay(double PrePay) {
        this.PrePay = PrePay;
    }

    public double getFinalPay() {
        return FinalPay;
    }

    public void setFinalPay(double FinalPay) {
        this.FinalPay = FinalPay;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int OrderType) {
        this.OrderType = OrderType;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getRefuseReson() {
        return RefuseReson;
    }

    public void setRefuseReson(String RefuseReson) {
        this.RefuseReson = RefuseReson;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public int getPhoneCount() {
        return PhoneCount;
    }

    public void setPhoneCount(int PhoneCount) {
        this.PhoneCount = PhoneCount;
    }

    public String getCallListUrl() {
        return CallListUrl;
    }

    public void setCallListUrl(String CallListUrl) {
        this.CallListUrl = CallListUrl;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String OrderName) {
        this.OrderName = OrderName;
    }

    public String getCallContent() {
        return CallContent;
    }

    public void setCallContent(String CallContent) {
        this.CallContent = CallContent;
    }

    public List<CallTasksBean> getCallTasks() {
        return CallTasks;
    }

    public void setCallTasks(List<CallTasksBean> CallTasks) {
        this.CallTasks = CallTasks;
    }

    public static class CallTasksBean {
        /**
         * CallResult : {"RunningID":1000000001,"ResultType":0,"Note":"无","AudioUrl":null}
         * CustomServer : null
         * RunningID : 1000000001
         * CallNo : 18696162662
         * Name : 何泽鹏
         * OrderID : 1000028
         * ServerID : null
         */

        private CallResultBean CallResult;
        private Object CustomServer;
        private int RunningID;
        private String CallNo;
        private String Name;
        private int OrderID;
        private Object ServerID;

        public CallResultBean getCallResult() {
            return CallResult;
        }

        public void setCallResult(CallResultBean CallResult) {
            this.CallResult = CallResult;
        }

        public Object getCustomServer() {
            return CustomServer;
        }

        public void setCustomServer(Object CustomServer) {
            this.CustomServer = CustomServer;
        }

        public int getRunningID() {
            return RunningID;
        }

        public void setRunningID(int RunningID) {
            this.RunningID = RunningID;
        }

        public String getCallNo() {
            return CallNo;
        }

        public void setCallNo(String CallNo) {
            this.CallNo = CallNo;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getOrderID() {
            return OrderID;
        }

        public void setOrderID(int OrderID) {
            this.OrderID = OrderID;
        }

        public Object getServerID() {
            return ServerID;
        }

        public void setServerID(Object ServerID) {
            this.ServerID = ServerID;
        }

        public static class CallResultBean {
            /**
             * RunningID : 1000000001
             * ResultType : 0
             * Note : 无
             * AudioUrl : null
             */

            private int RunningID;
            private int ResultType;
            private String Note;
            private Object AudioUrl;

            public int getRunningID() {
                return RunningID;
            }

            public void setRunningID(int RunningID) {
                this.RunningID = RunningID;
            }

            public int getResultType() {
                return ResultType;
            }

            public void setResultType(int ResultType) {
                this.ResultType = ResultType;
            }

            public String getNote() {
                return Note;
            }

            public void setNote(String Note) {
                this.Note = Note;
            }

            public Object getAudioUrl() {
                return AudioUrl;
            }

            public void setAudioUrl(Object AudioUrl) {
                this.AudioUrl = AudioUrl;
            }
        }
    }
}
