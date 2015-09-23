{ 
	"en" : "T_BASE_STATICS",
	"cn" : "报表语句",
	"formColumn" : 1,
	"btns" : [ {
		"btype" : "refresh"
	}, {
		"btype" : "add"
	}, {
		"btype" : "modify"
	}, {
		"btype" : "delete"
	} ],
	"columns" : [ {
		"isPk" : true,
		"allowBlank" : false,
		"dataIndex" : "BBID",
		"header" : "报表ID"
	},{
		"allowBlank" : false,
		"dataIndex" : "BBMC",
		"header" : "报表名称"
	},{
		"allowBlank" : false,
		"dataIndex" : "SQLID",
		"header" : "执行sqlid"
	},{
		"allowBlank" : false,
		"dataIndex" : "BBJY",
		"header" : "报表语句",
		"xtype" : "textarea"
	}]
}