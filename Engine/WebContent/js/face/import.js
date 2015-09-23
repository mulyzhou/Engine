(function(){
	var importJar = {
		author:'zdf'
	};
	
	importJar.form = new Ext.form.FormPanel({
	     baseCls : 'x-plain',
		 height : 100,
	     labelWidth : 70,
	     fileUpload : true,
	     defaultType : 'textfield',
	     bodyStyle : 'padding:20px',
	     items : [{
	        xtype : 'textfield',
	        fieldLabel : '子系统文件',
	        name : 'userfile',
	        id : 'userfile',
	        inputType : 'file',
	        blankText : '文件不能为空！',
	        anchor : '100%' // anchor width by percentage
	       }],
	     buttons : [{
	         text : '上传',
	         handler : function() {
					if(importJar.form.form.isValid()){
						if(Ext.getCmp('userfile').getValue() == ''){
							Ext.Msg.alert('错误','请选择你要上传的文件');
							return;
						};
						Ext.MessageBox.show({
							title : '请等待',
							msg : '文件正在上传...',
							progressText : '',
							width : 300,
							progress : true,
							closable : false,
							animEl : 'loding'
						});
						importJar.form.getForm().submit({
							url : 'import',
							method : 'POST',
							success : function(form, action) {
								Ext.Msg.alert('提示',action.result.msg);
							},
							failure : function() {
								Ext.Msg.alert('错误','系统导入失败');
							}
						});
					}
	         	}
	        }]
	 });

	importJar.center = new Ext.Panel({
		title : '子系统导入',
		layout : 'fit',
		items : [ importJar.form ]
	});
	
	return importJar;
})(Ext)
