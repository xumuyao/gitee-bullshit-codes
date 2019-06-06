/**
 * 处理业务的异常
 * 居然有一堆静态异常，准备好了随时可以抛？？
 * 错误码是字符串
 */

public class CommandException extends BaseException {

	private static final long serialVersionUID = -6354513454371927970L;
	
	public static CommandException PARAM_NULL= new CommandException("Command_assemble_01", "Parameter is Needed But Empty");
	public static CommandException DEVID_NULL = new CommandException("Command_assemble_02", "DevId Cannot Be Null");
	public static CommandException MDCODE_NULL = new CommandException("Command_assemble_03", "Model Code Cannot Be Empty");
	public static CommandException ORDER_NULL = new CommandException("Command_assemble_04", "Order Cannot Be Empty");
	public static CommandException TYPE_NULL = new CommandException("Command_assemble_05", "Upstream / Downstream Type Cannot Be Empty");
	public static CommandException MENUID_NULL = new CommandException("Command_assemble_06", "Menu Id Cannot Be Null");
	public static CommandException CTRLTYPE_NOT_RANGE= new CommandException("Command_assemble_07", "Ctrltype Cannot Be Recognized, Which is not in Range");
	public static CommandException CMD_NULL = new CommandException("Command_analyze_01", "CMD Cannot Be Null");
	public static CommandException PAYLOAD_NULL = new CommandException("Command_analyze_02", "Payload Cannot Be Null");
	public static CommandException FRAMEWORK_FAILED= new CommandException("Command_analyze_03", "Framework Failed to be Checked");
	public static CommandException CHECK_FAILED= new CommandException("Command_analyze_04", "Command Failed to be Checked");
	public static CommandException CONFIGURE_NOT_EXIST = new CommandException("Command_error_1001", "Configure is not Exist");
	public static CommandException REDIS_ERROR = new CommandException("Command_error_1002", "Cache Command in Redis Error", true);
	
	public CommandException() {
		super();
	}

	public CommandException(String errCode, String errMsg, boolean isSystem, Throwable cause) {
		super(errCode, errMsg, isSystem, cause);
	}

	public CommandException(String errCode, String errMsg) {
		super(errCode, errMsg);
	}

	public CommandException(String errCode, String errMsg, boolean isSystem) {
		super(errCode, errMsg, isSystem);
	}

	public CommandException(String errCode, String errMsg, Throwable cause) {
		super(errCode, errMsg, cause);
	}
