package com.dbiz.app.reportservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AppConstant {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DiscoveredDomainsApi {

		public static final String REPORT_SERVICE_GET_Individual_Industry_BY_TDI_ID = "http://REPORT-SERVICE/report-service/api/v1/individualIndustry/findAllByTaxDeclarationIndividualId";
		public static final String REPORT_SERVICE_GET_Tax_Declaration_Vat_Pit_Line_BY_TDI_ID = "http://REPORT-SERVICE/report-service/api/v1/taxDeclarationVatPitLine/findAllByTaxDeclarationIndividualId";
		public static final String REPORT_SERVICE_GET_Tax_Declaration_Excise_BY_TDI_ID = "http://REPORT-SERVICE/report-service/api/v1/taxDeclarationExcise/findAllByTaxDeclarationIndividualId";
		public static final String REPORT_SERVICE_GET_Tax_Declaration_Resource_Environment_BY_TDI_ID = "http://REPORT-SERVICE/report-service/api/v1/taxDeclarationResourceEnvironment/findAllByTaxDeclarationIndividualId";

		public static final String REPORT_SERVICE_GET_Environment_Fee_BY_ID = "http://REPORT-SERVICE/report-service/api/v1/environmentFee";
		public static final String REPORT_SERVICE_GET_Tax_Business_Industry_BY_ID = "http://REPORT-SERVICE/report-service/api/v1/taxBusinessIndustry";
		public static final String REPORT_SERVICE_GET_Inventory_Category_Special_Tax_BY_ID = "http://REPORT-SERVICE/report-service/api/v1/inventoryCategorySpecialTax";
		public static final String REPORT_SERVICE_GET_Tax_Payment_Method_BY_ID = "http://REPORT-SERVICE/report-service/api/v1/taxPaymentMethod";
		public static final String PRODUCT_SERVICE_GET_Business_Sector_Group_BY_ID = "http://PRODUCT-SERVICE/product-service/api/v1/businessSectorGroup";
		public static final String REPORT_SERVICE_GET_Expense_Type_BY_ID = "http://REPORT-SERVICE/report-service/api/v1/expenseTypes";

		public static final String REPORT_SERVICE_SAVE_ALL_Individual_Industry = "http://REPORT-SERVICE/report-service/api/v1/individualIndustry/saveAll";
		public static final String REPORT_SERVICE_SAVE_ALL_Tax_Declaration_Vat_Pit_Line = "http://REPORT-SERVICE/report-service/api/v1/taxDeclarationVatPitLine/saveAll";
		public static final String REPORT_SERVICE_SAVE_ALL_Tax_Declaration_Excise = "http://REPORT-SERVICE/report-service/api/v1/taxDeclarationExcise/saveAll";
		public static final String REPORT_SERVICE_SAVE_ALL_Tax_Declaration_Resource_Environment = "http://REPORT-SERVICE/report-service/api/v1/taxDeclarationResourceEnvironment/saveAll";

		public static final String GET_ORG_BY_ID = "http://TENANT-SERVICE/tenant-service/api/v1/org";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DeclarationResourceEnvironmentType {

		public static final String environmentalProtectionTaxDeclaration = "ETD";
		public static final String environmentalProtectionFeeDeclaration = "EFD";
		public static final String resourceTaxDeclaration = "RTD";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DefaultTaxDeclarationConfig {

		public static final String TAX_PAY_MT_DEF_TYPE = "TAX_PAY_MT_DEF_TYPE";
		public static final String APPLICABLE_CIRCULAR_DEFAULT = "APPLICABLE_CIRCULAR_DEFAULT";
	}
}









