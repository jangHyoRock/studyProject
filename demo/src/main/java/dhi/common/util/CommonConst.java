package dhi.common.util;

public class CommonConst {
	
	public final static String StringEmpty = "";
	
	// System Name
	public final static String SYSTEM_NAME_OPTIMIZER = "optimizer";
	public final static String SYSTEM_NAME_BTMS = "btms";
	
	// System Current Status Type
	public final static String SYSTEM_CURRENT_STATUS_NORMAL= "Normal";
	public final static String SYSTEM_CURRENT_STATUS_WARNING = "Warning";
	
	// Settings Info Type
	public final static String CONFING_SETTING_TYPE_BASELINE = "Baseline";
	public final static String CONFING_SETTING_TYPE_COAL = "Coal";
	public final static String CONFING_SETTING_TYPE_KPI = "KPI";
	public final static String CONFING_SETTING_TYPE_EFFICIENCY = "Efficiency";
	public final static String CONFING_SETTING_TYPE_OPTCONF = "OptConf";
	
	// Baseline Setting Info.
	public final static String BASELINE_RH_SPRAY_FOW_AVG = "rh_spray_flow_avg";
	public final static String BASELINE_FLUE_GAS_O2_AVG = "flue_gas_o2_avg";
	public final static String BASELINE_FLUE_GAS_CO_AVG = "flue_gas_co_avg"; 
	public final static String BASELINE_FLUE_GAS_TEMP_LR_DEV = "flue_gas_temp_l/r_dev";
	public final static String BASELINE_STACK_NOX_AVG = "stack_nox_avg";
	public final static String BASELINE_UNIT_HEAT_RATE = "unit_heat_rate";
	public final static String BASELINE_BLR_EFFICIENCY = "blr_efficiency";
	public final static String BASELINE_BLR_DRY_GAS_LOSS = "blr_dry_gas_loss";
	public final static String BASELINE_BASELINE_BLR_OTHER_LOSS = "blr_other_loss";
	public final static String BASELINE_TBN_CYCLE_HEAT_RATE = "tbn_cycle_heat_rate";
	public final static String BASELINE_TOTAL_AUX_POWER = "total_aux_power";
	
	// OptConfig Info.
	public final static String OPTCONF_TEMP_STATUS_NORMAL_EXCESS_RATIO = "temp_noraml_excess_ratio";
	public final static String OPTCONF_CO_STATUS_NORMAL_EXCESS_RATIO = "co_noraml_excess_ratio";
	public final static String OPTCONF_NOX_STATUS_NORMAL_EXCESS_RATIO = "nox_noraml_excess_ratio"; 
	public final static String OPTCONF_O2_STATUS_NORMAL_EXCESS_RATIO = "o2_noraml_excess_ratio";
		
	// Common Data Info.	
	public final static String ACTIVE_POWER_OF_GENERATOR = "N_ATT51"; // LOAD	
	public final static String APH_A_I_L_O2_NEW = "N_ATT82";
	public final static String APH_B_I_L_O2_NEW = "N_ATT118";	
	public final static String ECON_OUT_FLUE_GAS_O2_1_RIGHT = "N_ATT323";
	public final static String ECON_OUT_FLUE_GAS_O2_2_RIGHT = "N_ATT324";
	public final static String ECON_OUT_FLUE_GAS_OXYGE_1_LEFT = "N_ATT325";
	public final static String ECON_OUT_FLUE_GAS_OXYGE_2_LEFT = "N_ATT326";
	public final static String HRZN_FG_TEMP_L = "N_ATT858"; 
	public final static String HRZN_FG_TEMP_R = "N_ATT871";
	public final static String NOx = "N_ATT241";
	public final static String ECO_OUT_FLUE_GAS_CO_L = "N_ATT314"; // 10GAI.AI087804.PNT
	public final static String ECO_OUT_FLUE_GAS_CO_R = "N_ATT315"; // 10HAI.AI097804.PNT
	public final static String FREQUENCY_OF_GENERATOR = "N_ATT445"; // FSEQ
	public final static String FURNACE_DRAFT = "N_ATT797";	// FP
	public final static String TOTAL_COAL_FLOW = "N_ATT2143"; // CF
	public final static String TOTAL_AIR_FLOW = "N_ATT2142"; // AF
}
