package dhi.optimizer.common;

/**
 * Common Final Static Variable Class. <br>
 * : 공통 정적 변수 클래스.
 */
public class CommonConst {
	
	public final static String StringEmpty = "";
	
	// Opt Code GroupID
	public final static String OptCodeTrednCategoryGrouID = "trend_category";
	public final static String OptCodeNavigatorTrendCategoryGrouID = "navigator_trend_category";
	
	// Alarm
	public final static String AlarmNameNNMODEL = "NN Model";
	public final static String AlarmNameAlgorithmOutputController = "Output Data";
	
	// Opt Common Constant 
	public final static int TOTAL_COALFEEDER_COUNT = 7; 		// Total Burner Count 
	public final static int DEFAULT_PAGE_LIST_SIZE = 2000; 		// Page List Size
	public final static int DATE_SECONDS_OF_DAY = 86400; 		// Seconds Of Day
	public final static int DATE_SECONDS_OF_HOUR= 3600; 		// Seconds Of Hour
	
	// Settings Info Type
	public final static String CONFING_SETTING_TYPE_BASELINE = "Baseline";
	public final static String CONFING_SETTING_TYPE_COAL = "Coal";
	public final static String CONFING_SETTING_TYPE_KPI = "KPI";
	public final static String CONFING_SETTING_TYPE_EFFICIENCY = "Efficiency";
	public final static String CONFING_SETTING_TYPE_OPTCONF = "OptConf";
	
	//Coal Setting Info.
	public final static String TOTAL_MOISTURE = "total_moisture";
	public final static String ASH_CONTENTS = "ash_contents";
	public final static String VOLATILE_MATTER = "volatile_matter";
	public final static String FIXED_CARBON = "fixed_carbone";
	public final static String GROSS_CALORIFIC_VALUE = "gross_calorific_value";
	public final static String N = "n";
	public final static String C = "c";
	public final static String H = "h";
	public final static String S = "s";
	public final static String O = "o";
	
	//Efficiency Setting Info.
	public final static String TOTAL_RESIDUE_DISTRIBUTION_RATE_BOTTOM_ASH = "total_residue_distribution_rate_bottom_ash";
	public final static String TOTAL_RESIDUE_DISTRIBUTION_RATE_ECONOMIZER_HOPPER_ASH = "total_residue_distribution_rate_economizer_hopper_ash";
	public final static String UNBURNED_CARBON_IN_RESIDUE_BOTTOM_ASH = "unburned_carbon_in_residue_bottom_ash";
	public final static String UNBURNED_CARBON_IN_RESIDUE_ECONOMIZER_HOPPER_ASH = "unburned_carbon_in_residue_economizer_hopper_ash";
	public final static String FLY_ASH = "fly_ash";
	public final static String RELATIVE_HUMIDITY = "relative_humidity";
	public final static String ATMOSPHERIC_PRESSURE = "atmospheric_pressure";
	public final static String WET_ASH_PIT_LOSS = "wet_ash_pit_loss";
	public final static String PROJECTED_AREA_OF_HOPPER_OPENING = "projected_area_of_hopper_opening";
	public final static String SURFACE_RADIATION_CONVECTION = "surface_radiation_convection";
	public final static String UNMEASURED_LOSSES = "unmeasured_losses";
	public final static String AUXILIARY_DRIVES_POWER_CONSUMPTION = "auxiliary_drives_power_consumption";
	public final static String OVERALL_DRIVE_EFFICIENCY = "overall_drive_efficiency";
	public final static String _100_TMCR_MS_FLOW = "100_tmcr_ms_flow";
	public final static String IP_LP_TBN_FLOW_K_DESIGN = "ip_lp_tbn_flow_k_design";
	public final static String PIPING_LOSS_AS_HEAT_RATE = "piping_loss_as_heat_rate";
	public final static String MAKEUP_FLOW_LOSS_K = "make_up_flow_loss_k";	
	public final static String RH_SPRAY_LOSS_K = "rh_spray_loss_k";
	public final static String AMMONIA_SAVING_K = "ammonia_saving_k";
	
	//KPI Setting Info.
	public final static String ELECTRICITY_PRICE = "electricity_price";
	public final static String FUEL_PRICE = "fuel_price";
	public final static String AMMONIA_PRICE = "ammonia_price";
	public final static String CER_PRICE = "cer_price";
	
	//Baseline Setting Info.
	public final static String RH_SPRAY_FOW_AVG = "rh_spray_flow_avg";
	public final static String FLUE_GAS_O2_AVG = "flue_gas_o2_avg";
	public final static String FLUE_GAS_CO_AVG = "flue_gas_co_avg"; 
	public final static String FLUE_GAS_TEMP_LR_DEV = "flue_gas_temp_l/r_dev";
	public final static String STACK_NOX_AVG = "stack_nox_avg";
	public final static String UNIT_HEAT_RATE = "unit_heat_rate";
	public final static String BLR_EFFICIENCY = "blr_efficiency";
	public final static String BLR_DRY_GAS_LOSS = "blr_dry_gas_loss";
	public final static String BLR_OTHER_LOSS = "blr_other_loss";
	public final static String TBN_CYCLE_HEAT_RATE = "tbn_cycle_heat_rate";
	public final static String RH_SPRAY_LOSS = "rh_spray_loss";
	public final static String TOTAL_AUX_POWER = "total_aux_power";
	
	// OptConfig Info.
	public final static String OPTCONF_POLAR_OPTIMUM_ZONE_RATIO = "polar_optimum_zone_ratio";
	public final static String OPTCONF_POLAR_TEMP_BASELINE_RATIO = "polar_temp_baseline_ratio";
	public final static String OPTCONF_POLAR_NOX_BASELINE_RATIO = "polar_nox_baseline_ratio"; 
	public final static String OPTCONF_POLAR_O2_BASELINE_RATIO = "polar_o2_baseline_ratio";
	public final static String OPTCONF_POLAR_CO_BASELINE_RATIO = "polar_co_baseline_ratio";
	
	// Burner, OFA Min, Max Range.
	public final static double MIN_BURNER_POSITION = 25;
	public final static double MAX_BURNER_POSITION = 100;
	public final static double MIN_OFA_POSITION = 0;
	public final static double MAX_OFA_POSITION = 100;	
	
	// Report Id.
	public final static String REPORT_ITEM_MILL_MILL_A = "MILL_A";
	public final static String REPORT_ITEM_MILL_MILL_B = "MILL_B";
	public final static String REPORT_ITEM_MILL_MILL_C = "MILL_C";
	public final static String REPORT_ITEM_MILL_MILL_D = "MILL_D";
	public final static String REPORT_ITEM_MILL_MILL_E = "MILL_E";
	public final static String REPORT_ITEM_MILL_MILL_F = "MILL_F";
	public final static String REPORT_ITEM_MILL_MILL_G = "MILL_G";
	public final static String REPORT_ITEM_MILL_MILL_COAL_FLOW = "COAL_FLOW";
	public final static String REPORT_ITEM_MILL_MILL_GEN_MW = "GEN_MW";
	public final static String REPORT_ITEM_NORMAL_RH_SPRAY = "RH_SPRAY";
	public final static String REPORT_ITEM_NORMAL_FG_O2 = "FG_O2";
	public final static String REPORT_ITEM_NORMAL_FG_TEMP_DEV = "FG_TEMP_DEV";
	public final static String REPORT_ITEM_NORMAL_NOX = "NOX";
	public final static String REPORT_ITEM_NORMAL_BLR_EFFICIENCY = "BLR_EFFICIENCY";
	public final static String REPORT_ITEM_NORMAL_UNIT_HEATRATE = "UNIT_HEATRATE";
	public final static String REPORT_ITEM_NORMAL_AUX_POWER = "AUX_POWER";
	public final static String REPORT_ITEM_NORMAL_TOTAL_MOISTURE = "TOTAL_MOISTRUE";
	public final static String REPORT_ITEM_NORMAL_ASH_CONTENTS = "ASH_CONTENTS";
	public final static String REPORT_ITEM_NORMAL_VOLATILE_MATTER = "VOLATILE_MATTER";
	public final static String REPORT_ITEM_NORRAL_FIXED_CARBONE = "FIXED_CARBONE";
	public final static String REPORT_ITEM_NORMAL_GROSS_CALORIFIC_VALUE ="GROSS_CALORIFIC_VALUE";	
	
	//OP Data Info.
	public final static String TOTAL_COAL_FLOW = "N_ATT2143"; // 10GFUEL.AI_TCF.PNT
	
	public final static String COAL_FEEDER_A_FEEDRATE = "N_ATT270"; // 10GAI.AI086102.PNT
	public final static String COAL_FEEDER_B_FEEDRATE = "N_ATT273"; // 10GAI.AI086106.PNT
	public final static String COAL_FEEDER_C_FEEDRATE = "N_ATT277"; // 10GAI.AI086202.PNT
	public final static String COAL_FEEDER_D_FEEDRATE = "N_ATT280"; // 10GAI.AI086206.PNT
	public final static String COAL_FEEDER_E_FEEDRATE = "N_ATT284"; // 10HAI.AI096102.PNT
	public final static String COAL_FEEDER_F_FEEDRATE = "N_ATT287"; // 10HAI.AI096106.PNT
	public final static String COAL_FEEDER_G_FEEDRATE = "N_ATT291"; // 10HAI.AI096202.PNT
	
	public final static String FD_A_INLET_AIR_TEMP = "N_ATT350"; // 10GAI.AI080607.PNT
	public final static String FD_B_INLET_AIR_TEMP = "N_ATT354"; // 10HAI.AI090607.PNT
	public final static String PA_FAN_A_INLET_AIR_TEMP = "N_ATT1482"; // 10GAI.AI083305.PNT
	public final static String PA_FAN_B_INLET_AIR_TEMP = "N_ATT1486"; // 10HAI.AI093305.PNT
	
	public final static String AIR_PREHEATER_A_INLET_SA_TEMP1 = "N_ATT57"; // 10GAI.AI088105.PNT
	public final static String AIR_PREHEATER_A_INLET_SA_TEMP2 = "N_ATT58"; // 10GAI.AI088205.PNT
	public final static String AIR_PREHEATER_B_INLET_SA_TEMP1 = "N_ATT63"; // 10HAI.AI098105.PNT
	public final static String AIR_PREHEATER_B_INLET_SA_TEMP2 = "N_ATT64"; // 10HAI.AI098205.PNT
	
	public final static String AIR_PREHEATER_A_INLET_PA_TEMP1 = "N_ATT55"; // 10GAI.AI083307.PNT
	public final static String AIR_PREHEATER_A_INLET_PA_TEMP2 = "N_ATT56"; // 10GAI.AI084507.PNT // Duplicated Name
	public final static String AIR_PREHEATER_B_INLET_PA_TEMP1 = "N_ATT62"; // 10HAI.AI093307.PNT
	public final static String AIR_PREHEATER_B_INLET_PA_TEMP2 = "N_ATT60"; // 10HAI.AI094507.PNT	
	
	public final static String MILL_A_INL_PRI_AIR_FLOW = "N_ATT1171"; // 10GAMILL.SEL100_2.OUT
	public final static String MILL_B_INL_PRI_AIR_FLOW = "N_ATT1203"; // 10GBMILL.SEL100_2.OUT
	public final static String MILL_C_INL_PRI_AIR_FLOW = "N_ATT1234"; // 10GCMILL.SEL100_2.OUT
	public final static String MILL_D_INL_PRI_AIR_FLOW = "N_ATT1266"; // 10GDMILL.SEL100_2.OUT
	public final static String MILL_E_INL_PRI_AIR_FLOW = "N_ATT1303"; // 10HEMILL.SEL100_2.OUT
	public final static String MILL_F_INL_PRI_AIR_FLOW = "N_ATT1344"; // 10HFMILL.SEL100_2.OUT
	public final static String MILL_G_INL_PRI_AIR_FLOW = "N_ATT1375"; // 10HGMILL.SEL100_2.OUT
	
	public final static String MILL_A_INL_PRI_AIR_FLOW_2 = "N_ATT1168"; // 10GAMILL.MLA_PAFLOW.PNT
	public final static String MILL_B_INL_PRI_AIR_FLOW_2 = "N_ATT1200"; // 10GBMILL.MLB_PAFLOW.PNT
	public final static String MILL_C_INL_PRI_AIR_FLOW_2 = "N_ATT1231"; // 10GCMILL.MLC_PAFLOW.PNT
	public final static String MILL_D_INL_PRI_AIR_FLOW_2 = "N_ATT1263"; // 10GDMILL.MLD_PAFLOW.PNT
	public final static String MILL_E_INL_PRI_AIR_FLOW_2 = "N_ATT1300"; // 10HEMILL.MLE_PAFLOW.PNT
	public final static String MILL_F_INL_PRI_AIR_FLOW_2 = "N_ATT1341"; // 10HFMILL.MLF_PAFLOW.PNT
	public final static String MILL_G_INL_PRI_AIR_FLOW_2 = "N_ATT1372"; // 10HGMILL.MLG_PAFLOW.PNT	
	
	public final static String APH_A_I_L_O2_NEW = "N_ATT82"; // 10KAI.AI102508.PNT
	public final static String APH_B_I_L_O2_NEW = "N_ATT118"; // 10KAI.AI103508.PNT
	public final static String ECON_OUT_FLUE_GAS_O2_1_RIGHT = "N_ATT323"; // 10HAI.AI097104.PNT
	public final static String ECON_OUT_FLUE_GAS_O2_2_RIGHT = "N_ATT324"; // 10HAI.AI097204.PNT
	public final static String ECON_OUT_FLUE_GAS_OXYGE_1_LEFT = "N_ATT325"; // 10KAI.AI102505.PNT
	public final static String ECON_OUT_FLUE_GAS_OXYGE_2_LEFT = "N_ATT326"; // 10KAI.AI103505.PNT
	
	public final static String APH_A_OUTLET_FLUE_GAS_OXYGEN = "N_ATT97"; // 10GAI.AI087706.PNT
	public final static String APH_B_OUTLET_FLUE_GAS_OXYGEN = "N_ATT136"; // 10HAI.AI097706.PNT	
	
	public final static String APH_A_INLET_FLUE_GAS_TEMP_1 = "N_ATT84"; // 10GAI.AI085709.PNT
	public final static String APH_A_INLET_FLUE_GAS_TEMP_2 = "N_ATT85"; // 10GAI.AI085809.PNT
	public final static String APH_B_INLET_FLUE_GAS_TEMP_1 = "N_ATT120"; // 10HAI.AI095709.PNT
	public final static String APH_B_INLET_FLUE_GAS_TEMP_2 = "N_ATT121"; // 10HAI.AI095809.PNT
	
	public final static String APH_A_OUTLET_FLUE_GAS_TEMP_1 = "N_ATT98"; // 10GAI.AI088106.PNT
	public final static String APH_A_OUTLET_FLUE_GAS_TEMP_2 = "N_ATT99"; // 10GAI.AI088206.PNT
	public final static String APH_B_OUTLET_FLUE_GAS_TEMP_1 = "N_ATT137"; // 10HAI.AI098106.PNT
	public final static String APH_B_OUTLET_FLUE_GAS_TEMP_2 = "N_ATT138"; // 10HAI.AI098206.PNT
	
	public final static String FURNACE_WINDBOX_DP_LEFT = "N_ATT809"; // 10DAI.AI042405.PNT
	public final static String FURNACE_PA_DIFF_PRESS_LEFT = "N_ATT803"; // 10DAI.AI042407.PNT
	// public final static String FURNACE_WINDBOX_DP_LEFT = "N_ATT810"; // Duplicated Name
	public final static String FURNACE_WINDBOX_DP_RIGHT = "N_ATT816"; // 10DAI.AI042802.PNT
	public final static String FURNACE_PA_DP_RIGHT = "N_ATT804"; // 10DAI.AI042803.PNT
	public final static String LEFT_WINBOX_PRESS = "N_ATT1011"; // 10DAI.AI042804.PNT
	public final static String RIGHT_WINBOX_PRESS = "N_ATT1816"; // 10DAI.AI042805.PNT
	public final static String CN1_AA_SA_DMPR_POS = "N_ATT1417"; // 10DAI.AI043101.PNT
	public final static String CN1_AB_SA_DMPR_POS = "N_ATT1418"; // 10DAI.AI043102.PNT
	public final static String CN1_BC_SA_DMPR_POS = "N_ATT1420"; // 10DAI.AI043103.PNT
	public final static String CN1_CD_SA_DMPR_POS = "N_ATT1422"; // 10DAI.AI043104.PNT
	public final static String CN1_DE_SA_DMPR_POS = "N_ATT1424"; // 10DAI.AI043105.PNT
	public final static String CN1_EF_SA_DMPR_POS = "N_ATT1426"; // 10DAI.AI043106.PNT
	public final static String CN1_FG_SA_DMPR_POS = "N_ATT1428"; // 10DAI.AI043107.PNT
	public final static String CN1_GG_SA_DMPR_POS = "N_ATT1430"; // 10DAI.AI043108.PNT
	public final static String W_REAR_UOFA2_DMPR_POS = "N_ATT1596"; // 10DAI.AI043109.PNT
	public final static String W_REAR_UOFA1_DMPR_POS = "N_ATT1595"; // 10DAI.AI043110.PNT
	public final static String W_REAR_UOFA3_DMPR_POS = "N_ATT1597"; // 10DAI.AI043111.PNT	
	public final static String CN2_AA_SA_DMPR_POS = "N_ATT1433"; // 10DAI.AI043201.PNT
	public final static String CN2_AB_SA_DMPR_POS = "N_ATT1434"; // 10DAI.AI043202.PNT
	public final static String CN2_BC_SA_DMPR_POS = "N_ATT1436"; // 10DAI.AI043203.PNT
	public final static String CN2_CD_SA_DMPR_POS = "N_ATT1438"; // 10DAI.AI043204.PNT
	public final static String CN2_DE_SA_DMPR_POS = "N_ATT1440"; // 10DAI.AI043205.PNT
	public final static String CN2_EF_SA_DMPR_POS = "N_ATT1442"; // 10DAI.AI043206.PNT
	public final static String CN2_FG_SA_DMPR_POS = "N_ATT1444"; // 10DAI.AI043207.PNT
	public final static String CN2_GG_SA_DMPR_POS = "N_ATT1446"; // 10DAI.AI043208.PNT
	public final static String W_FRT_UOFA1_DMPR_POS = "N_ATT498"; // 10DAI.AI043209.PNT
	public final static String W_FRT_UOFA2_DMPR_POS = "N_ATT499"; // 10DAI.AI043210.PNT
	public final static String W_FRT_UOFA3_DMPR_POS = "N_ATT500"; // 10DAI.AI043211.PNT
	public final static String CN3_AA_SA_DMPR_POS = "N_ATT1448"; // 10DAI.AI043301.PNT
	public final static String CN3_AB_SA_DMPR_POS = "N_ATT1449"; // 10DAI.AI043302.PNT
	public final static String CN3_BC_SA_DMPR_POS = "N_ATT1451"; // 10DAI.AI043303.PNT
	public final static String CN3_CD_SA_DMPR_POS = "N_ATT1453"; // 10DAI.AI043304.PNT
	public final static String CN3_DE_SA_DMPR_POS = "N_ATT1455"; // 10DAI.AI043305.PNT
	public final static String CN3_EF_SA_DMPR_POS = "N_ATT1457"; // 10DAI.AI043306.PNT
	public final static String CN3_FG_SA_DMPR_POS = "N_ATT1459"; // 10DAI.AI043307.PNT
	public final static String CN3_GG_SA_DMPR_POS = "N_ATT1461"; // 10DAI.AI043308.PNT
	public final static String W_RT_UOFA1_DMPR_POS = "N_ATT1811"; // 10DAI.AI043309.PNT
	public final static String W_RT_UOFA2_DMPR_POS = "N_ATT1812"; // 10DAI.AI043310.PNT
	public final static String W_RT_UOFA3_DMPR_POS = "N_ATT1813"; // 10DAI.AI043311.PNT
	public final static String CN4_AA_SA_DMPR_POS = "N_ATT1463"; // 10DAI.AI043401.PNT
	public final static String CN4_AB_SA_DMPR_POS = "N_ATT1464"; // 10DAI.AI043402.PNT
	public final static String CN4_BC_SA_DMPR_POS = "N_ATT1466"; // 10DAI.AI043403.PNT
	public final static String CN4_CD_SA_DMPR_POS = "N_ATT1468"; // 10DAI.AI043404.PNT
	public final static String CN4_DE_SA_DMPR_POS = "N_ATT1470"; // 10DAI.AI043405.PNT
	public final static String CN4_EF_SA_DMPR_POS = "N_ATT1472"; // 10DAI.AI043406.PNT
	public final static String CN4_FG_SA_DMPR_POS = "N_ATT1474"; // 10DAI.AI043407.PNT
	public final static String CN4_GG_SA_DMPR_POS = "N_ATT1476"; // 10DAI.AI043408.PNT
	public final static String W_LFT_UOFA3_DMPR_POS = "N_ATT1008"; // 10DAI.AI043409.PNT
	public final static String W_LFT_UOFA2_DMPR_POS = "N_ATT1007"; // 10DAI.AI043410.PNT
	public final static String W_LFT_UOFA1_DMPR_POS = "N_ATT1006"; // 10DAI.AI043411.PNT
	public final static String CN1_A_SA_DMPR_POS = "N_ATT1416"; // 10DAI.AI044101.PNT
	public final static String CN1_B_SA_DMPR_POS = "N_ATT1419"; // 10DAI.AI044102.PNT
	public final static String CN1_C_SA_DMPR_POS = "N_ATT1421"; // 10DAI.AI044103.PNT
	public final static String CN1_D_SA_DMPR_POS = "N_ATT1423"; // 10DAI.AI044104.PNT
	public final static String CN1_E_SA_DMPR_POS = "N_ATT1425"; // 10DAI.AI044105.PNT
	public final static String CN1_F_SA_DMPR_POS = "N_ATT1427"; // 10DAI.AI044106.PNT
	public final static String CN1_G_SA_DMPR_POS = "N_ATT1429"; // 10DAI.AI044107.PNT
	public final static String CN3_A_SA_DMPR_POS = "N_ATT1447"; // 10DAI.AI044108.PNT
	public final static String CN3_B_SA_DMPR_POS = "N_ATT1450"; // 10DAI.AI044109.PNT
	public final static String CN3_C_SA_DMPR_POS = "N_ATT1452"; // 10DAI.AI044110.PNT
	public final static String CN3_D_SA_DMPR_POS = "N_ATT1454"; // 10DAI.AI044111.PNT
	public final static String CN3_E_SA_DMPR_POS = "N_ATT1456"; // 10DAI.AI044112.PNT
	public final static String CN3_F_SA_DMPR_POS = "N_ATT1458"; // 10DAI.AI044113.PNT
	public final static String CN3_G_SA_DMPR_POS = "N_ATT1460"; // 10DAI.AI044114.PNT
	public final static String CN2_A_SA_DMPR_POS = "N_ATT1432"; // 10DAI.AI044201.PNT
	public final static String CN2_B_SA_DMPR_POS = "N_ATT1435"; // 10DAI.AI044202.PNT
	public final static String CN2_C_SA_DMPR_POS = "N_ATT1437"; // 10DAI.AI044203.PNT
	public final static String CN2_D_SA_DMPR_POS = "N_ATT1439"; // 10DAI.AI044204.PNT
	public final static String CN2_E_SA_DMPR_POS = "N_ATT1441"; // 10DAI.AI044205.PNT
	public final static String CN2_F_SA_DMPR_POS = "N_ATT1443"; // 10DAI.AI044206.PNT
	public final static String CN2_G_SA_DMPR_POS = "N_ATT1445"; // 10DAI.AI044207.PNT
	public final static String CN4_A_SA_DMPR_POS = "N_ATT1462"; // 10DAI.AI044208.PNT
	public final static String CN4_B_SA_DMPR_POS = "N_ATT1465"; // 10DAI.AI044209.PNT
	public final static String CN4_C_SA_DMPR_POS = "N_ATT1467"; // 10DAI.AI044210.PNT
	public final static String CN4_D_SA_DMPR_POS = "N_ATT1469"; // 10DAI.AI044211.PNT
	public final static String CN4_E_SA_DMPR_POS = "N_ATT1471"; // 10DAI.AI044212.PNT
	public final static String CN4_F_SA_DMPR_POS = "N_ATT1473"; // 10DAI.AI044213.PNT
	public final static String CN4_G_SA_DMPR_POS = "N_ATT1475"; // 10DAI.AI044214.PNT	
	public final static String LEFT_WALL_UOFA_FLOW = "N_ATT1010"; // 10DAI.AI042806.PNT
	public final static String RIGHT_WALL_UOFA_FLOW = "N_ATT1815"; // 10DAI.AI042807.PNT
	public final static String REAR_WALL_UOFA_FLOW = "N_ATT1599"; //10DAI.AI046507.PNT
	public final static String FRONT_WALL_UOFA_FLOW = "N_ATT502"; // 10DAI.AI046506.PNT	
	public final static String WB_Press_L = "N_ATT1012"; // 10DAI.AI046504.PNT
	public final static String WB_Press_R = "N_ATT1817"; // 10DAI.AI046505.PNT
	public final static String FNC_WB_DP = "N_ATT805"; // 10DSWIND.AI042405B.PNT
	public final static String BOILER_LOAD5 = "N_ATT196"; // 10EAI.AI054204.PNT // 보일러 출력(%)
	public final static String BOILER_LOAD6 = "N_ATT197"; // 10EAI.AI054205.PNT // 보일러 출력(%) 
	
	public final static String CN1_A_Coal_Flame = "N_ATT5"; // 10EAI.AI054501.PNT
	public final static String CN1_B_Coal_Flame = "N_ATT7"; // 10EAI.AI054502.PNT
	public final static String CN1_C_Coal_Flame = "N_ATT9"; // 10EAI.AI054503.PNT
	public final static String CN1_D_Coal_Flame = "N_ATT10"; // 10EAI.AI054504.PNT
	public final static String CN1_E_Coal_Flame = "N_ATT12"; // 10EAI.AI054505.PNT
	public final static String CN1_F_Coal_Flame = "N_ATT13"; // 10EAI.AI054506.PNT
	public final static String CN1_G_Coal_Flame = "N_ATT15"; // 10EAI.AI054507.PNT
	
	public final static String CN2_A_Coal_Flame = "N_ATT17"; // 10EAI.AI054601.PNT
	public final static String CN2_B_Coal_Flame = "N_ATT19"; // 10EAI.AI054602.PNT
	public final static String CN2_C_Coal_Flame = "N_ATT21"; // 10EAI.AI054603.PNT
	public final static String CN2_D_Coal_Flame = "N_ATT22"; // 10EAI.AI054604.PNT
	public final static String CN2_E_Coal_Flame = "N_ATT24"; // 10EAI.AI054605.PNT
	public final static String CN2_F_Coal_Flame = "N_ATT25"; // 10EAI.AI054606.PNT
	public final static String CN2_G_Coal_Flame = "N_ATT27"; // 10EAI.AI054607.PNT
	
	public final static String CN3_A_Coal_Flame = "N_ATT29"; // 10EAI.AI054701.PNT
	public final static String CN3_B_Coal_Flame = "N_ATT31"; // 10EAI.AI054702.PNT
	public final static String CN3_C_Coal_Flame = "N_ATT33"; // 10EAI.AI054703.PNT
	public final static String CN3_D_Coal_Flame = "N_ATT34"; // 10EAI.AI054704.PNT
	public final static String CN3_E_Coal_Flame = "N_ATT36"; // 10EAI.AI054705.PNT
	public final static String CN3_F_Coal_Flame = "N_ATT37"; // 10EAI.AI054706.PNT
	public final static String CN3_G_Coal_Flame = "N_ATT39"; // 10EAI.AI054707.PNT
	
	public final static String CN4_A_Coal_Flame = "N_ATT40"; // 10EAI.AI054801.PNT
	public final static String CN4_B_Coal_Flame = "N_ATT42"; // 10EAI.AI054802.PNT
	public final static String CN4_C_Coal_Flame = "N_ATT44"; // 10EAI.AI054803.PNT
	public final static String CN4_D_Coal_Flame = "N_ATT45"; // 10EAI.AI054804.PNT
	public final static String CN4_E_Coal_Flame = "N_ATT47"; // 10EAI.AI054805.PNT
	public final static String CN4_F_Coal_Flame = "N_ATT48"; // 10EAI.AI054806.PNT
	public final static String CN4_G_Coal_Flame = "N_ATT50"; // 10EAI.AI054807.PNT
	
	public final static String BOILER_LOAD1 = "N_ATT192"; // 10FAI.AI120109.PNT
	public final static String FD_A_IL_Air_Temp = "N_ATT350"; // 10GAI.AI080607.PNT
	public final static String HRZN_FG_temp_L = "N_ATT858"; // 10GAI.AI085707.PNT
	public final static String FINAL_RH_OL_FG_temp_L = "N_ATT1639"; // 10GAI.AI085708.PNT
	public final static String HRZN_FG_temp_R = "N_ATT871"; // 10GAI.AI085807.PNT
	public final static String HRZN_FG_DRAFT_L = "N_ATT869"; // 10GAI.AI087105.PNT
	public final static String HRZN_FG_DRAFT_R = "N_ATT870"; // 10GAI.AI087205.PNT
	public final static String PRM_SH_IN_FG_Press_L = "N_ATT1537"; // 10GAI.AI087301.PNT
	public final static String PRM_SH_OUT_FG_Press_L = "N_ATT1541"; // 10GAI.AI087302.PNT
	public final static String PRM_RH_IN_FG_Press_L = "N_ATT1535"; // 10GAI.AI087401.PNT
	public final static String PRM_RH_OUT_FG_Press_L = "N_ATT1539"; // 10GAI.AI087402.PNT
	public final static String FINAL_RH_OL_FG_Press_L = "N_ATT1637"; // 10GAI.AI087801.PNT
	public final static String ECON_OL_FG_Press_L = "N_ATT327"; // 10GAI.AI087802.PNT
	
	public final static String NOx = "N_ATT241"; // 10HAI.AI095401.PNT
	public final static String SOx = "N_ATT243"; // 10HAI.AI095402.PNT
	public final static String Stack_CO = "N_ATT261"; // 10HAI.AI095403.PNT
	public final static String Stack_O2 = "N_ATT1478"; //10HAI.AI095406.PNT
	public final static String ECO_OUT_FLUE_GAS_CO_L = "N_ATT314"; // 10GAI.AI087804.PNT
	public final static String ECO_OUT_FLUE_GAS_CO_R = "N_ATT315"; // 10HAI.AI097804.PNT
	public final static String FG_Flow = "N_ATT416"; // 10HAI.AI095408.PNT
	public final static String FG_Temp = "N_ATT420"; // 10HAI.AI095409.PNT
	public final static String FINAL_RH_OL_FG_temp_R = "N_ATT1640"; // 10HAI.AI095708.PNT
	public final static String RGT_FG_ADJ_DMPR_A_POS = "N_ATT1025"; // 10KAI.AI103804.PNT, (10HAI.AI096205.PNT에서 변경되었음)
	public final static String RGT_FG_ADJ_DMPR_B_POS = "N_ATT1026"; // 10KAI.AI103805.PNT, (10HAI.AI096206.PNT에서 변경되었음)
	public final static String RGT_FG_ADJ_DMPR_C_POS = "N_ATT1027"; // 10KAI.AI103806.PNT, (10HAI.AI096207.PNT에서 변경되었음)	
	public final static String ECON_OL_FG_O2_1_R = "N_ATT323"; // 10HAI.AI097104.PNT
	public final static String ECON_OL_FG_O2_2_R = "N_ATT324"; // 10HAI.AI097204.PNT
	public final static String PRM_SH_IL_FG_Press_R = "N_ATT1538"; // 10HAI.AI097301.PNT
	public final static String PRM_SH_OL_FG_Press_R = "N_ATT1542"; // 10HAI.AI097302.PNT
	public final static String PRM_RH_IL_FG_Press_R = "N_ATT1536"; // 10HAI.AI097401.PNT
	public final static String PRM_RH_OL_FG_Press_R = "N_ATT1540"; // 10HAI.AI097402.PNT
	public final static String FINAL_RH_OL_FG_Press_R = "N_ATT1638"; // 10HAI.AI097801.PNT
	public final static String ECON_OL_FG_Press_R = "N_ATT328"; // 10HAI.AI097802.PNT
	public final static String TOTAL_AIR_FLOW = "N_ATT2142"; // 10HFDF.AI_TAF.PNT
	public final static String O2 = "N_ATT320"; // 10HFDF.OUT_DIS_017.PNT
	//public final static String O2,_Check_필요 = "N_ATT321"; // 10HFDF.SS_021.INP1
	//public final static String O2,_Check_필요 = "N_ATT322"; // 10HFDF.SS_021.INP2
	//public final static String NOx,_Check_필요 = "N_ATT242"; // 10HIFB.AI095401.PNT
	//public final static String SOx,_Check_필요 = "N_ATT244"; // 10HIFB.AI095402.PNT
	//public final static String CO2,_Check_필요 = "N_ATT240"; // 10HIFB.AI095404.PNT
	//public final static String Dust,_Check_필요 = "N_ATT313"; // 10HIFB.AI095407.PNT
	//public final static String CO2,_Check_필요 = "N_ATT266"; // 10HIFB.AIN_137.PNT
	public final static String ECON_OL_FG_O2_1_L = "N_ATT325"; // 10KAI.AI102505.PNT
	
	public final static String RH_M_DSH_SPRAY_FLOW_L_1 = "N_ATT1693"; // 10KAI.AI103301.PNT
	public final static String RH_M_DSH_SPRAY_FLOW_L_2 = "N_ATT1694"; // 10KAI.AI104301.PNT
	public final static String RH_M_DSH_SPRAY_FLOW_L_3 = "N_ATT1695"; // 10KRHM_A.AI103301B.PNT
	public final static String RH_M_DSH_SPRAY_FLOW_L_4 = "N_ATT1696"; // 10KRHM_A.S1013.INP1
	public final static String RH_M_DSH_SPRAY_FLOW_L_5 = "N_ATT1697"; // 10KRHM_A.S1013.INP2

	public final static String RH_M_DSH_SPRAY_FLOW_R_1 = "N_ATT1699"; // 10KAI.AI103302.PNT
	public final static String RH_M_DSH_SPRAY_FLOW_R_2 = "N_ATT1698"; // 10KAI.AI104302.PNT
	public final static String RH_M_DSH_SPRAY_FLOW_R_3 = "N_ATT1700"; // 10KRHM_B.AI103302B.PNT
	public final static String RH_M_DSH_SPRAY_FLOW_R_4 = "N_ATT1701"; // 10KRHM_B.S1013.INP1
	public final static String RH_M_DSH_SPRAY_FLOW_R_5 = "N_ATT1702"; // 10KRHM_B.S1013.INP2

	public final static String RH_E_DSH_SPRAY_FLOW_L_1 = "N_ATT1628"; // 10KAI.AI103303.PNT
	public final static String RH_E_DSH_SPRAY_FLOW_L_2 = "N_ATT1629"; // 10KAI.AI104303.PNT
	public final static String RH_E_DSH_SPRAY_FLOW_L_3 = "N_ATT1630"; // 10KRHE_A.AI103303B.PNT
	public final static String RH_E_DSH_SPRAY_FLOW_L_4 = "N_ATT1631"; // 10KRHE_A.S1013.INP1
	public final static String RH_E_DSH_SPRAY_FLOW_L_5 = "N_ATT1632"; // 10KRHE_A.S1013.INP2

	public final static String RH_E_DSH_SPRAY_FLOW_R_1 = "N_ATT1623"; // 10KAI.AI103304.PNT
	public final static String RH_E_DSH_SPRAY_FLOW_R_2 = "N_ATT1624"; // 10KAI.AI104304.PNT
	public final static String RH_E_DSH_SPRAY_FLOW_R_3 = "N_ATT1625"; // 10KRHE_B.AI103304B.PNT
	public final static String RH_E_DSH_SPRAY_FLOW_R_4 = "N_ATT1626"; // 10KRHE_B.S1013.INP1
	public final static String RH_E_DSH_SPRAY_FLOW_R_5 = "N_ATT1627"; // 10KRHE_B.S1013.INP2
		
	public final static String RH_MICROFLOW_DSH_SPRAY_FLOW_L = "N_ATT1695"; // 10KRHM_A.AI103301B.PNT
	public final static String RH_MICROFLOW_DSH_SPRAY_FLOW_R = "N_ATT1700"; // 10KRHM_B.AI103302B.PNT
	
	public final static String HORIZON_GAS_DUCT_FLUE_GAS_T_L = "N_ATT858"; // 10GAI.AI085707.PNT
	public final static String HORIZON_GAS_DUCT_FLUE_GAS_T_R = "N_ATT871"; // 10GAI.AI085807.PNT
	
	public final static String ECON_OL_FG_O2_2_L = "N_ATT326"; // 10KAI.AI103505.PNT
	public final static String MEGA_WATT = "N_ATT4"; // 10KAI.AI103801.PNT // Turbine 출력	
	public final static String LOAD_REFERENCE = "N_ATT3"; // 10KAI.AI103802.PNT  // LOAD REFERENCE(%)	
	public final static String LFT_FG_ADJ_DMPR_A_POS = "N_ATT1616"; // 10HAI.AI096205.PNT, (10KAI.AI103804.PNT에서 변경하였음.)
	public final static String LFT_FG_ADJ_DMPR_B_POS = "N_ATT1617"; // 10HAI.AI096206.PNT, (10KAI.AI103805.PNT에서 변경하였음.)
	public final static String LFT_FG_ADJ_DMPR_C_POS = "N_ATT1618"; // 10HAI.AI096207.PNT, (10KAI.AI103806.PNT에서 변경하였음.)	
	//public final static String RH_M_DSH_SPRAY_FLOW_R = "N_ATT1698"; // 10KAI.AI104302.PNT // Duplicated Name
	//public final static String RH_E_DSH_SPRAY_FLOW_L = "N_ATT1629"; // 10KAI.AI104303.PNT // Duplicated Name
	//public final static String RH_E_DSH_SPRAY_FLOW_R = "N_ATT1624"; // 10KAI.AI104304.PNT // Duplicated Name	
	public final static String FINAL_RH_OL_STM_TEMP_L = "N_ATT1606"; // 10KAI.AI105302.PNT
	public final static String FINAL_RH_OL_STM_TEMP_R = "N_ATT1611"; // 10KAI.AI105308.PNT
	public final static String ULD_LOCAL_SET_POINT = "N_ATT1122"; // 10KMAINC.3PID.SPT
	public final static String ACTIVE_POWER_OF_GENERATOR1 = "N_ATT53"; // 10KMAINC.SEL10091.INP1 // 발전소 출력(MW)
	public final static String ACTIVE_POWER_OF_GENERATOR2 = "N_ATT54"; // 10KMAINC.SEL10091.INP2 // 발전소 출력(MW)
	public final static String SH_FINISH_OL_TEMP_L = "N_ATT1883"; // 10LAI.AI112510.PNT
	public final static String SH_FINISH_OL_TEMP_R = "N_ATT1879"; // 10LAI.AI111511.PNT
	//public final static String SH_FINISH_OL_TEMP_L = "N_ATT1882"; // 10LAI.AI111510.PNT // Duplicated Name
	//public final static String SH_FINISH_OL_temp_R = "N_ATT1880"; // 10LAI.AI112511.PNT // Duplicated Name
	
	public final static String SH_DSH1_SPRAY_FLOW_1_L = "N_ATT2060"; // 10LAI.AI114206.PNT
	public final static String SH_DSH1_SPRAY_FLOW_2_L = "N_ATT2061"; // 10LAI.AI114306.PNT
	public final static String SH_DSH1_SPRAY_FLOW_3_L = "N_ATT2062"; // 10LPSH_A.AI114206B.PNT
	public final static String SH_DSH1_SPRAY_FLOW_4_L = "N_ATT2063"; // 10LPSH_A.S1013.INP1
	public final static String SH_DSH1_SPRAY_FLOW_5_L = "N_ATT2064"; // 10LPSH_A.S1013.INP2	
	
	public final static String SH_DSH1_SPRAY_FLOW_1_R = "N_ATT2091"; // 10LAI.AI114207.PNT	
	public final static String SH_DSH1_SPRAY_FLOW_2_R = "N_ATT2092"; // 10LAI.AI114307.PNT
	public final static String SH_DSH1_SPRAY_FLOW_3_R = "N_ATT2093"; // 10LPSH_B.AI114207B.PNT
	
	public final static String SH_DSH2_SPRAY_FLOW_1_R = "N_ATT2122"; // 10LAI.AI115203.PNT
	public final static String SH_DSH2_SPRAY_FLOW_2_R = "N_ATT2123"; // 10LAI.AI115302.PNT
	public final static String SH_DSH2_SPRAY_FLOW_3_R = "N_ATT2124"; // 10LFSH_B.AI115203B.PNT
	public final static String SH_DSH2_SPRAY_FLOW_4_R = "N_ATT2125"; // 10LFSH_B.S1013.INP1
	public final static String SH_DSH2_SPRAY_FLOW_5_R = "N_ATT2126"; // 10LFSH_B.S1013.INP2
	
	public final static String SH_FINISH_OL_PRESS_L = "N_ATT1871"; // 10LAI.AI115205.PNT
	public final static String SH_FINISH_OL_PRESS_R = "N_ATT1876"; // 10LAI.AI115206.PNT
	
	public final static String SH_DSH2_SPRAY_FLOW_1_L = "N_ATT2065"; // 10LAI.AI115204.PNT
	public final static String SH_DSH2_SPRAY_FLOW_2_L = "N_ATT2066"; // 10LAI.AI115303.PNT
	public final static String SH_DSH2_SPRAY_FLOW_3_L = "N_ATT2067"; // 10LFSH_A.AI115204B.PNT
	public final static String SH_DSH2_SPRAY_FLOW_4_L = "N_ATT2068"; // 10LFSH_A.S1013.INP1		
	public final static String SH_DSH2_SPRAY_FLOW_5_L = "N_ATT2069"; // 10LFSH_A.S1013.INP2
	
	public final static String SH_FINISH_OL_PRESS_R_NEED_CHECK = "N_ATT1877"; // 10LAI.AI115304.PNT
	public final static String TOTAL_FEEDWATER_TEMP = "N_ATT382"; // 10MFW.AI103101B.PNT
	public final static String TOTAL_FEEDWATER_FLOW = "N_ATT2144"; // 10MFW.AI133501T.PNT // = MAIN_STEAM_FLOW

	public final static String MAIN_STEAM_TEMP = "N_ATT1107"; // 10KAI.AI105301.PNT
	public final static String MAIN_STEAM_PRESS_1 = "N_ATT1106"; // 10KAI.AI103401.PNT
	public final static String FEED_WATER_TEMP_1 = "N_ATT385"; // 10MAI.AI130101.PNT
	public final static String FEED_WATER_TEMP_2 = "N_ATT402"; // 10MAI.AI130201.PNT
	public final static String MAIN_FEEDWATER_PRESS = "N_ATT1096"; // 10MAI.AI134601.PNT , FeedWater Pressure 
	public final static String BOILER_END_FEEDWATER_PRESS = "N_ATT1"; // 10MAI.AI134602.PNT , FeedWater Pressure

	public final static String RH_FINISH_OL_STM_TEMP_LEFT_1 = "N_ATT1605"; // 10KAI.AI103208.PNT // Hot RH Steam Temperature
	public final static String RH_FINISH_OL_STM_TEMP_LEFT_2 = "N_ATT1606"; // 10KAI.AI105302.PNT // Hot RH Steam Temperature
	public final static String RH_FINISH_OL_STM_TEMP_RIGHT_1 = "N_ATT1610"; // 10KAI.AI104208.PNT // Hot RH Steam Temperature
	public final static String RH_FINISH_OL_STM_TEMP_RIGHT_2 = "N_ATT1611"; // 10KAI.AI105308.PNT // Hot RH Steam Temperature
	public final static String FINISH_RH_OUTLET_STEAM_PRESS_LEFT_1 = "N_ATT412"; // 10KRHE_A.SEL11671A.INP1 // Hot RH Steam Pressure
	public final static String FINISH_RH_OUTLET_STEAM_PRESS_LEFT_2 = "N_ATT413"; // 10KRHE_A.SEL11671A.INP2 // Hot RH Steam Pressure
	public final static String FINISH_RH_OUTLET_STEAM_PRESS_RIGHT_1 = "N_ATT414"; // 10KRHE_A.SEL11671B.INP1 // Hot RH Steam Pressure
	public final static String FINISH_RH_OUTLET_STEAM_PRESS_RIGHT_2 = "N_ATT415"; // 10KRHE_A.SEL11671B.INP2 // Hot RH Steam Pressure

	public final static String LT_RH_IN_HDR_IL_STM_TEMP_LEFT_1 = "N_ATT1060"; // 10KAI.AI103209.PNT // Cold RH Steam Temperature
	public final static String LT_RH_IN_HDR_IL_STM_TEMP_RIGHT_1 = "N_ATT1056"; // 10KAI.AI103210.PNT // Cold RH Steam Temperature
	public final static String LT_RH_IN_HDR_IL_STM_TEMP_LEFT_2 = "N_ATT1061"; // 10KAI.AI104209.PNT // Cold RH Steam Temperature
	public final static String LT_RH_IN_HDR_IL_STM_TEMP_RIGHT_2 = "N_ATT1065"; // 10KAI.AI104210.PNT // Cold RH Steam Temperature
	public final static String LT_RH_IN_HDR_IL_STM_PRESS_LEFT = "N_ATT1054"; // 10KAI.AI102503.PNT // Cold RH Steam Pressure
	public final static String LT_RH_IN_HDR_IL_STM_PRESS_RIGHT = "N_ATT1055"; // 10KAI.AI103503.PNT // Cold RH Steam Pressure

	public final static String RH_EMER_DSH_SPRAY_COMMON_PIPE_T = "N_ATT1622"; // 10KAI.AI104402.PNT // RH Spray WTR Temperature
	public final static String RH_MICROFLOW_DSH_SPRAY_WTR_TEMP = "N_ATT1703"; // 10KAI.AI104401.PNT // RH Spray WTR Temperature
	public final static String RH_EMER_DSH_SPRAY_COMMON_PIPE_P = "N_ATT1621"; // 10KAI.AI104503.PNT // RH Spray WTR Pressure
	public final static String RH_MICROFLOW_DSH_SPRAY_WTR_PRESS = "N_ATT1706"; // 10KAI.AI104502.PNT // RH Spray WTR Pressure
	public final static String RH_SPRAY_WTR_FLOW = "N_ATT1707"; // 10MFW.MATH102_14.RO01 // RH SPRAY WTR FLOW
	
	public final static String ACTIVE_POWER_OF_GENERATOR = "N_ATT51"; // 10PAI.AI16B402.PNT
	//public final static String MAKEUP_FLOW; // No Tag
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_1 = "N_ATT714"; // 10MAI.AIR48501.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_2 = "N_ATT715"; // 10MAI.AIR48502.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_3 = "N_ATT716"; // 10MAI.AIR48503.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_4 = "N_ATT717"; // 10MAI.AIR48504.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_5 = "N_ATT718"; // 10MAI.AIR48505.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_6 = "N_ATT719"; // 10MAI.AIR48506.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_7 = "N_ATT720"; // 10MAI.AIR48507.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_8 = "N_ATT721"; // 10MAI.AIR48508.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_9 = "N_ATT722"; // 10MAI.AIR48509.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_10 = "N_ATT723"; // 10MAI.AIR48510.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_11 = "N_ATT724"; // 10MAI.AIR48511.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_12 = "N_ATT725"; // 10MAI.AIR48512.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_13 = "N_ATT726"; // 10MAI.AIR48601.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_14 = "N_ATT727"; // 10MAI.AIR48602.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_15 = "N_ATT728"; // 10MAI.AIR48603.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_16 = "N_ATT729"; // 10MAI.AIR48604.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_17 = "N_ATT730"; // 10MAI.AIR48605.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_18 = "N_ATT731"; // 10MAI.AIR48606.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_19 = "N_ATT732"; // 10MAI.AIR48607.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_20 = "N_ATT456"; // 10MAI.AIR49610.PNT
	public final static String FN_LT_SPRL_EVAP_MTL_TEMP_21 = "N_ATT457"; // 10MAI.AIR49611.PNT

	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP2 = "N_ATT691"; // 10MAI.AIR48102.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP4 = "N_ATT692"; // 10MAI.AIR48104.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP6 = "N_ATT693"; // 10MAI.AIR48106.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP8 = "N_ATT694"; // 10MAI.AIR48108.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP10 = "N_ATT695"; // 10MAI.AIR48110.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP12 = "N_ATT696"; // 10MAI.AIR48112.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP14 = "N_ATT697"; // 10MAI.AIR48202.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP16 = "N_ATT698"; // 10MAI.AIR48204.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP17 = "N_ATT669"; // 10MAI.AIR48205.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP18 = "N_ATT699"; // 10MAI.AIR48206.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP20 = "N_ATT700"; // 10MAI.AIR48208.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP21 = "N_ATT801"; // 10MAI.AIR48209.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP22 = "N_ATT446"; // 10MAI.AIR48801.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP24 = "N_ATT447"; // 10MAI.AIR48803.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP26 = "N_ATT448"; // 10MAI.AIR48805.PNT
	public final static String FN_FRT_SPRL_EVAP_MTL_TEMP29 = "N_ATT449"; // 10MAI.AIR48808.PNT
	
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP1 = "N_ATT750"; // 10MAI.AIR49301.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP2 = "N_ATT751"; // 10MAI.AIR49302.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP3 = "N_ATT752"; // 10MAI.AIR49303.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP4 = "N_ATT753"; // 10MAI.AIR49304.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP5 = "N_ATT754"; // 10MAI.AIR49305.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP6 = "N_ATT755"; // 10MAI.AIR49306.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP7 = "N_ATT756"; // 10MAI.AIR49307.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP8 = "N_ATT757"; // 10MAI.AIR49308.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP9 = "N_ATT758"; // 10MAI.AIR49309.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP10 = "N_ATT759"; // 10MAI.AIR49310.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP12 = "N_ATT760"; // 10MAI.AIR49312.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP13 = "N_ATT761"; // 10MAI.AIR49401.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP14 = "N_ATT762"; // 10MAI.AIR49402.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP15 = "N_ATT763"; // 10MAI.AIR49403.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP16 = "N_ATT764"; // 10MAI.AIR49404.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP18 = "N_ATT765"; // 10MAI.AIR49406.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP20 = "N_ATT766"; // 10MAI.AIR49408.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP22 = "N_ATT475"; // 10MAI.AIR49806.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP24 = "N_ATT476"; // 10MAI.AIR49808.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP26 = "N_ATT477"; // 10MAI.AIR49810.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP28 = "N_ATT478"; // 10MAI.AIR4A401.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP29 = "N_ATT479"; // 10MAI.AIR4A402.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP29_2 = "N_ATT767"; // 10MAI.AIR49813.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP30 = "N_ATT480"; // 10MAI.AIR4A403.PNT
	public final static String FN_REAR_SPRL_EVAP_MTL_TEMP30_2 = "N_ATT687"; // 10MAI.AIR49410.PNT
		
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_2 = "N_ATT770"; // 10MAI.AIR4A102.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_4 = "N_ATT771"; // 10MAI.AIR4A104.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_6 = "N_ATT772"; // 10MAI.AIR4A106.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_8 = "N_ATT773"; // 10MAI.AIR4A108.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_10 = "N_ATT774"; // 10MAI.AIR4A110.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_12 = "N_ATT775"; // 10MAI.AIR4A112.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_14 = "N_ATT776"; // 10MAI.AIR4A202.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_16 = "N_ATT777"; // 10MAI.AIR4A204.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_18 = "N_ATT778"; // 10MAI.AIR4A206.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_20 = "N_ATT485"; // 10MAI.AIR4A503.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_22 = "N_ATT486"; // 10MAI.AIR4A505.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_24 = "N_ATT487"; // 10MAI.AIR4A507.PNT
	public final static String FN_RT_SPRL_EVAP_MTL_TEMP_26 = "N_ATT488"; // 10MAI.AIR4A509.PNT

	// Performance & Efficiency - OFA Flow Info.	
	public final static String LEFTWALL_OFA_FLOW = "N_ATT1009"; // 10DSOF2.AI042806B.PNT
	public final static String RIGHTWALL_OFA_FLOW = "N_ATT1814"; // 10DSOF2.AI042807B.PNT
	public final static String FRONTWALL_OFA_FLOW = "N_ATT501"; // 10DSOF2.AI046506B.PNT
	public final static String REARWALL_OFA_FLOW = "N_ATT1598"; // 10DSOF2.AI046507B.PNT
	
	// Combustion Status - OFA Supply Info.	
	public final static String LEFTWALL_UOFA3_DAMPER_OPEN = "N_ATT1008"; // 10DAI.AI043409.PNT
	public final static String LEFTWALL_UOFA2_DAMPER_OPEN = "N_ATT1007"; // 10DAI.AI043410.PNT
	public final static String LEFTWALL_UOFA1_DAMPER_OPEN = "N_ATT1006"; // 10DAI.AI043411.PNT
	public final static String RIGHTWALL_UOFA3_DAMPER_OPEN = "N_ATT1811"; // 10DAI.AI043309.PNT
	public final static String RIGHTWALL_UOFA2_DAMPER_OPEN = "N_ATT1812"; // 10DAI.AI043310.PNT
	public final static String RIGHTWALL_UOFA1_DAMPER_OPEN = "N_ATT1813"; // 10DAI.AI043311.PNT
	public final static String REARWALL_UOFA3_DAMPER_OPEN = "N_ATT1596"; // 10DAI.AI043109.PNT
	public final static String REARWALL_UOFA2_DAMPER_OPEN = "N_ATT1595"; // 10DAI.AI043110.PNT
	public final static String REARWALL_UOFA1_DAMPER_OPEN = "N_ATT1597"; // 10DAI.AI043111.PNT
	public final static String FRONTWALL_UOFA3_DAMPER_OPEN = "N_ATT498"; // 10DAI.AI043209.PNT
	public final static String FRONTWALL_UOFA2_DAMPER_OPEN = "N_ATT499"; // 10DAI.AI043210.PNT
	public final static String FRONTWALL_UOFA1_DAMPER_OPEN = "N_ATT500"; // 10DAI.AI043211.PNT
	
	//Combustion Status - Coal PA/SA Supply Info.
	public final static String G_COAL = "N_ATT1008"; // 10DAI.AI043409.PNT
	public final static String F_COAL = "N_ATT287"; // 10HAI.AI096106.PNT
	public final static String E_COAL = "N_ATT284"; // 10HAI.AI096102.PNT
	public final static String D_COAL = "N_ATT280"; // 10GAI.AI086206.PNT
	public final static String C_COAL = "N_ATT277"; // 10GAI.AI086202.PNT
	public final static String B_COAL = "N_ATT273"; // 10GAI.AI086106.PNT
	public final static String A_COAL = "N_ATT270"; // 10GAI.AI086102.PNT
	
	public final static String G_PA = "N_ATT1375"; // 10HGMILL.SEL100_2.OUT
	public final static String F_PA = "N_ATT1344"; // 10HFMILL.SEL100_2.OUT
	public final static String E_PA = "N_ATT1303"; // 10GCMILL.SEL100_2.OUT
	public final static String D_PA = "N_ATT1266"; // 10GDMILL.SEL100_2.OUT
	public final static String C_PA = "N_ATT1234"; // 10GCMILL.SEL100_2.OUT
	public final static String B_PA = "N_ATT1203"; // 10GBMILL.SEL100_2.OUT
	public final static String A_PA = "N_ATT1171"; // 10GAMILL.SEL100_2.OUT
	
	public final static String G_SAD_C1 = "N_ATT1429"; // 10DAI.AI044107.PNT
	public final static String F_SAD_C1 = "N_ATT1427"; // 10DAI.AI044106.PNT
	public final static String E_SAD_C1 = "N_ATT1425"; // 10DAI.AI044105.PNT
	public final static String D_SAD_C1 = "N_ATT1423"; // 10DAI.AI044104.PNT
	public final static String C_SAD_C1 = "N_ATT1421"; // 10DAI.AI044103.PNT
	public final static String B_SAD_C1 = "N_ATT1419"; // 10DAI.AI044102.PNT
	public final static String A_SAD_C1 = "N_ATT1416"; // 10DAI.AI044101.PNT

	public final static String G_SAD_C2 = "N_ATT1445"; // 10DAI.AI044207.PNT
	public final static String F_SAD_C2 = "N_ATT1443"; // 10DAI.AI044206.PNT
	public final static String E_SAD_C2 = "N_ATT1441"; // 10DAI.AI044205.PNT
	public final static String D_SAD_C2 = "N_ATT1439"; // 10DAI.AI044204.PNT
	public final static String C_SAD_C2 = "N_ATT1437"; // 10DAI.AI044203.PNT
	public final static String B_SAD_C2 = "N_ATT1435"; // 10DAI.AI044202.PNT
	public final static String A_SAD_C2 = "N_ATT1432"; // 10DAI.AI044201.PNT

	public final static String G_SAD_C3 = "N_ATT1460"; // 10DAI.AI044114.PNT
	public final static String F_SAD_C3 = "N_ATT1458"; // 10DAI.AI044113.PNT
	public final static String E_SAD_C3 = "N_ATT1456"; // 10DAI.AI044112.PNT
	public final static String D_SAD_C3 = "N_ATT1454"; // 10DAI.AI044111.PNT
	public final static String C_SAD_C3 = "N_ATT1452"; // 10DAI.AI044110.PNT
	public final static String B_SAD_C3 = "N_ATT1450"; // 10DAI.AI044109.PNT
	public final static String A_SAD_C3 = "N_ATT1447"; // 10DAI.AI044108.PNT

	public final static String G_SAD_C4 = "N_ATT1475"; // 10DAI.AI044214.PNT
	public final static String F_SAD_C4 = "N_ATT1473"; // 10DAI.AI044213.PNT
	public final static String E_SAD_C4 = "N_ATT1471"; // 10DAI.AI044212.PNT
	public final static String D_SAD_C4 = "N_ATT1469"; // 10DAI.AI044211.PNT
	public final static String C_SAD_C4 = "N_ATT1467"; // 10DAI.AI044210.PNT
	public final static String B_SAD_C4 = "N_ATT1465"; // 10DAI.AI044209.PNT
	public final static String A_SAD_C4 = "N_ATT1462"; // 10DAI.AI044208.PNT
	
	public final static String GG_DAMPER_OPEN_C1 = "N_ATT1430"; // 10DAI.AI043108.PNT
	public final static String FG_DAMPER_OPEN_C1 = "N_ATT1428"; // 10DAI.AI043107.PNT
	public final static String EF_DAMPER_OPEN_C1 = "N_ATT1426"; // 10DAI.AI043106.PNT
	public final static String DE_DAMPER_OPEN_C1 = "N_ATT1424"; // 10DAI.AI043105.PNT
	public final static String CD_DAMPER_OPEN_C1 = "N_ATT1422"; // 10DAI.AI043104.PNT
	public final static String BC_DAMPER_OPEN_C1 = "N_ATT1420"; // 10DAI.AI043103.PNT
	public final static String AB_DAMPER_OPEN_C1 = "N_ATT1418"; // 10DAI.AI043102.PNT
	public final static String AA_DAMPER_OPEN_C1 = "N_ATT1417"; // 10DAI.AI043101.PNT

	public final static String GG_DAMPER_OPEN_C2 = "N_ATT1446"; // 10DAI.AI043208.PNT
	public final static String FG_DAMPER_OPEN_C2 = "N_ATT1444"; // 10DAI.AI043207.PNT
	public final static String EF_DAMPER_OPEN_C2 = "N_ATT1442"; // 10DAI.AI043206.PNT
	public final static String DE_DAMPER_OPEN_C2 = "N_ATT1440"; // 10DAI.AI043205.PNT
	public final static String CD_DAMPER_OPEN_C2 = "N_ATT1438"; // 10DAI.AI043204.PNT
	public final static String BC_DAMPER_OPEN_C2 = "N_ATT1436"; // 10DAI.AI043203.PNT
	public final static String AB_DAMPER_OPEN_C2 = "N_ATT1434"; // 10DAI.AI043202.PNT
	public final static String AA_DAMPER_OPEN_C2 = "N_ATT1433"; // 10DAI.AI043201.PNT

	public final static String GG_DAMPER_OPEN_C3 = "N_ATT1461"; // 10DAI.AI043308.PNT
	public final static String FG_DAMPER_OPEN_C3 = "N_ATT1459"; // 10DAI.AI043307.PNT
	public final static String EF_DAMPER_OPEN_C3 = "N_ATT1457"; // 10DAI.AI043306.PNT
	public final static String DE_DAMPER_OPEN_C3 = "N_ATT1455"; // 10DAI.AI043305.PNT
	public final static String CD_DAMPER_OPEN_C3 = "N_ATT1453"; // 10DAI.AI043304.PNT
	public final static String BC_DAMPER_OPEN_C3 = "N_ATT1451"; // 10DAI.AI043303.PNT
	public final static String AB_DAMPER_OPEN_C3 = "N_ATT1449"; // 10DAI.AI043302.PNT
	public final static String AA_DAMPER_OPEN_C3 = "N_ATT1448"; // 10DAI.AI043301.PNT

	public final static String GG_DAMPER_OPEN_C4 = "N_ATT1476"; // 10DAI.AI043408.PNT
	public final static String FG_DAMPER_OPEN_C4 = "N_ATT1474"; // 10DAI.AI043407.PNT
	public final static String EF_DAMPER_OPEN_C4 = "N_ATT1472"; // 10DAI.AI043406.PNT
	public final static String DE_DAMPER_OPEN_C4 = "N_ATT1470"; // 10DAI.AI043405.PNT
	public final static String CD_DAMPER_OPEN_C4 = "N_ATT1468"; // 10DAI.AI043404.PNT
	public final static String BC_DAMPER_OPEN_C4 = "N_ATT1466"; // 10DAI.AI043403.PNT
	public final static String AB_DAMPER_OPEN_C4 = "N_ATT1464"; // 10DAI.AI043402.PNT
	public final static String AA_DAMPER_OPEN_C4 = "N_ATT1463"; // 10DAI.AI043401.PNT 
	
	// RH Spray Supply	
	public final static String RH_FN_OL_STM_TEMP_RIGHT = "N_ATT1612"; // 10KRHM_B.AI104208B.PNT // Final RH Right Temp
	public final static String RH_FN_OL_STM_TEMP_LEFT = "N_ATT1607"; // 10KRHM_A.AI103208B.PNT // Final RH Left Temp

	public final static String RH_MICRO_DSH_OL_STM_TEMP_LEFT = "N_ATT1690"; // 10KRHM_A.AI103206B.PNT // RH Left 2nd Spray Outlet Temp
	public final static String RH_MICRO_DSH_OL_STM_TEMP_RIGHT = "N_ATT1678"; // 10KRHM_B.AI103207B.PNT // RH Right 2nd Spray Outlet Temp
	
	public final static String RH_MICRO_DSH_IL_STM_TEMP_LEFT = "N_ATT1685"; // 10KRHE_A.AI103204B.PNT // RH Left 2nd Spray Inlet Temp.
	public final static String RH_MICRO_DSH_IL_STM_TEMP_RIGHT = "N_ATT1673"; // 10KRHE_B.AI103205B.PNT // RH Right 2nd Spray Inlet Temp	 
	
	public final static String LT_RH_IN_HDR_IL_STM_TEMP_LEFT = "N_ATT1062"; // 10KRHE_A.AI103209B.PNT. // RH Left 1st Spray Outlet Temp.
	public final static String LT_RH_IN_HDR_IL_STM_TEMP_RIGHT = "N_ATT1057"; // 10KRHE_B.AI103210B.PNT // RH Right 1st Spray Outlet Temp 

	public final static String HP_CYLYNDER_EXHED_STM_TEMP = "N_ATT2190"; // 10NAI.AI146106.PNT // RH Right 1st Spray Inlet Temp,  RH Left 1st Spray Inlet Temp 
		
	// Navigator SA Flow
	public final static String APH_A_OUTLET_SA_FLOW = "N_ATT101"; // 10HFDF.AI087207B.PNT;
	public final static String APH_B_OUTLET_SA_FLOW = "N_ATT140"; // 10HFDF.AI097207B.PNT;
	
	//**********************************************************************************************
	public final static String TOTAL_AUX_POWER_VAL = "N_ATT2189"; // Need to Mapping At Sasan
	//**********************************************************************************************
	
	// Output Controller Control Data Tag.	
	public final static String CTR_DATA_CURRENT_O2_SP = "C_ATT8"; // 10CURRENT_O2_SP_VALUE	
	public final static String CTR_DATA_OUTPUT_BIAS_INITIALIZE = "C_ATT54"; // 10SOLUTION_OUTPUT_BIAS_INITIALIZE
	
	// System Control Data Tag
	public final static String CTR_DATA_SOLUTION_SYSTEM_FAULT_ON = "C_ATT26"; // 10SOLUTION_SYSTEM_FAULT_ON
	public final static String CTR_DATA_SOLUTION_CONTROL_READY = "C_ATT27"; // 10SOLUTION_CONTROL_READY
	public final static String CTR_DATA_SOLUTION_CONTROL_RUNNING = "C_ATT28"; // 10SOLUTION_CONTROL_RUNNING
	public final static String CTR_DATA_SOLUTION_CONTROL_STOPPED = "C_ATT29"; // 10SOLUTION_CONTROL_STOPPED	
	public final static String CTR_DATA_PROFIT_MODE_SELECTED = "C_ATT30"; // 10PROFIT_MODE_SELECTED
	public final static String CTR_DATA_EMISSION_MODE_SELECTED = "C_ATT31"; // 10EMISSION_MODE_SELECTED
	public final static String CTR_DATA_EQUIPMENT_MODE_SELECTED = "C_ATT32"; // 10EQUIPMENT_MODE_SELECTED
	
	// Efficiency Control Data Tag
	public final static String CTR_DATA_CURRENT_BOILER_EFFICIENCY = "C_ATT52"; // 10CURRENT_BOILER_EFFICIENCY
	public final static String CTR_DATA_CURRENT_UNIT_HEAT_RATE = "C_ATT53"; // 10CURRENT_UNIT_HEAT_RATE	
	
	public final static String CTR_DATA_BLR_PROCESS_DANGER_ALARM_ON = "C_ATT24"; // 10BLR_PROCESS_DANGER_ALARM_ON
	public final static String CTR_DATA_DCS_HEARTBEAT_FOR_MODBUS = "C_ATT1"; // 10DCS_HEARTBEAT_FOR_MODBUS
	public final static String CTR_DATA_SOLUTION_HEARTBIT_FOR_MODBUS = "C_ATT25"; // 10SOLUTION_HEARTBIT_FOR_MODBUS	
	
	public final static String CTR_DATA_SA_DAMPER_CNR1_BIAS_DMD = "C_ATT36"; // 10SA_DAMPER_CNR1_BIAS_DMD
	public final static String CTR_DATA_SA_DAMPER_CNR2_BIAS_DMD = "C_ATT37"; // 10SA_DAMPER_CNR2_BIAS_DMD
	public final static String CTR_DATA_SA_DAMPER_CNR3_BIAS_DMD = "C_ATT38"; // 10SA_DAMPER_CNR3_BIAS_DMD
	public final static String CTR_DATA_SA_DAMPER_CNR4_BIAS_DMD = "C_ATT39"; // 10SA_DAMPER_CNR4_BIAS_DMD
	
	public final static String CTR_DATA_OFA_DAMPER_LEFT_WALL_BIAS_DMD = "C_ATT40"; // 10OFA_DAMPER_LEFT_WALL_BIAS_DMD
	public final static String CTR_DATA_OFA_DAMPER_FRONT_WALL_BIAS_DMD = "C_ATT41"; // 10OFA_DAMPER_FRONT_WALL_BIAS_DMD
	public final static String CTR_DATA_OFA_DAMPER_RIGHT_WALL_BIAS_DMD = "C_ATT42"; // 10OFA_DAMPER_RIGHT_WALL_BIAS_DMD
	public final static String CTR_DATA_OFA_DAMPER_REAR_WALL_BIAS_DMD = "C_ATT43"; // 10OFA_DAMPER_REAR_WALL_BIAS_DMD
	
	public final static String CTR_DATA_OPTIMIZER_O2_TRIM_BIAS_DMD = "C_ATT33"; // 10OPTIMIZER_O2_TRIM_BIAS_DMD
	
	public final static String CTR_DATA_SA_DAMPER_CNR1_BIAS_HOLD = "C_ATT44"; // 10SA_DAMPER_CNR1_BIAS_HOLD
	public final static String CTR_DATA_SA_DAMPER_CNR2_BIAS_HOLD = "C_ATT45"; // 10SA_DAMPER_CNR2_BIAS_HOLD
	public final static String CTR_DATA_SA_DAMPER_CNR3_BIAS_HOLD = "C_ATT46"; // 10SA_DAMPER_CNR3_BIAS_HOLD
	public final static String CTR_DATA_SA_DAMPER_CNR4_BIAS_HOLD = "C_ATT47"; // 10SA_DAMPER_CNR4_BIAS_HOLD
	
	public final static String CTR_DATA_OFA_DAMPER_LEFT_WALL_BIAS_HOLD = "C_ATT48"; // 10OFA_DAMPER_LEFT_WALL_BIAS_HOLD
	public final static String CTR_DATA_OFA_DAMPER_FRONT_WALL_BIAS_HOLD = "C_ATT49"; // 10OFA_DAMPER_FRONT_WALL_BIAS_HOLD
	public final static String CTR_DATA_OFA_DAMPER_RIGHT_WALL_BIAS_HOLD = "C_ATT50"; // 10OFA_DAMPER_RIGHT_WALL_BIAS_HOLD
	public final static String CTR_DATA_OFA_DAMPER_REAR_WALL_BIAS_HOLD = "C_ATT51"; // 10OFA_DAMPER_REAR_WALL_BIAS_HOLD
	
	public final static String CTR_DATA_OPTIMIZER_O2_TRIM_BIAS_HOLD = "C_ATT35"; // 10OPTIMIZER_O2_TRIM_BIAS_HOLD
	
	public final static String CTR_DATA_OPTIMIZER_OUTPUT_CTRL_FULL_HOLD = "C_ATT34"; // 10OPTIMIZER_OUTPUT_CTRL_FULL_HOLD
}