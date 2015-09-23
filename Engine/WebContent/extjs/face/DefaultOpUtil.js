Ext.apply(Ext, {
	fFaceUtil : {
		kindeditorSync : function(me) {
			for ( var i = 0; i < me.columns.length; i++) {// 遍历
				if (me.columns[i]["isPk"]) {// 对主键进行处理
					continue;
				}
				
				if (me.columns[i]["isForm"] == false) {// 不做为表单项
					continue;
				}
				
				if(me.columns[i]["xtype"] == "kindeditor"){
					me.form.getForm().findField(me.columns[i]["dataIndex"]).editor.sync();
				}
			}
		},
		
		kindeditorInit : function(me){
			for ( var i = 0; i < me.columns.length; i++) {// 遍历
				if (me.columns[i]["isPk"]) {// 对主键进行处理
					continue;
				}
				
				if (me.columns[i]["isForm"] == false) {// 不做为表单项
					continue;
				}
				
				if(me.columns[i]["xtype"] == "kindeditor" && me.form.getForm().findField(me.columns[i]["dataIndex"])){
					me.form.getForm().findField(me.columns[i]["dataIndex"]).flyingInit();
				}
			}
		},
		
		flyingResizeInit : function(me){
			for ( var i = 0; i < me.columns.length; i++) {// 遍历
				if (me.columns[i]["isPk"]) {// 对主键进行处理
					continue;
				}
				
				if (me.columns[i]["isForm"] == false) {// 不做为表单项
					continue;
				}
				
				if(me.form.getForm().findField(me.columns[i]["dataIndex"]) && me.form.getForm().findField(me.columns[i]["dataIndex"])["flyingResize"] != null){
					me.form.getForm().findField(me.columns[i]["dataIndex"])["flyingResize"]();
				}
			}
		},
		
		clickModify : function(me){			
			if(me.grid.getSelectionModel().hasSelection()){
				var rows = me.grid.getSelectionModel().getSelections();
				if(rows.length == 1){
					if (me.beforeModifyShow() == false){
						return false;
					}
					
					if(me.formInit){//表单初始化
						me.formInit();
					}
					
					me.opType = "modify";
					
					var row = rows[0];
				
					me.params[me.pk] = row.data[me.pk];//获取主键值

					for(var i =0;i<me.columns.length;i++){//遍历表单赋值
						if (me.columns[i]["isPk"]) {// 对主键进行处理
							continue;
						}
						var name = me.columns[i].dataIndex;
						if(name == undefined){
							continue;
						}else if(me.columns[i].xtype == "departmentselection" && me.form.getForm().findField("NAME_"+name) != undefined){
							me.form.getForm().findField("NAME_"+name).setValue(row.data[name]);
						}else if(me.columns[i].xtype == "combo" && me.form.getForm().findField(name) != undefined && me.form.getForm().findField(name).mode == "remote"){
							var st = me.form.getForm().findField(name).getStore();
						
							var mark = true;
							st.each(function(record){

								if(record.data[name] == row.data[name]){
									mark = false;
								}
							});
						
							if(mark){
								var comboValue = me.columns[i]["dataIndex"];
								var comboName = comboValue;
							
								if(comboValue.indexOf("ID") > 0){
									comboName = comboValue.replace("ID","NAME");
									if(row.data[comboName] == null){
										comboName = me.columns[i]["displayField"];
									}
								}else{
									comboName = comboValue + "MC";
									if(row.data[comboName] == null){
										comboName = me.columns[i]["displayField"];
									}
								}
							
							
								var newRecord = new Ext.data.Record();
								newRecord.data[me.columns[i]["displayField"]] = row.data[comboName];
								newRecord.data[me.columns[i]["valueField"]] = row.data[comboValue];
							
								st.insert(0,newRecord);
							}
						
							me.form.getForm().findField(name).setValue(row.data[name]);
						}else if(me.form.getForm().findField(name) != undefined){
							me.form.getForm().findField(name).setValue(row.data[name]);
						}
					
					}
					return true;
				}else{
					Ext.Msg.show({
						title : '友情提示',
						msg : '您选择了多条记录，请只选择一条记录进行修改操作！',
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.WARNING
					});
					return false;
				}
			}else{
				Ext.Msg.show({
					title : '友情提示',
					msg : '请选择一条记录进行修改操作！',
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.WARNING
				});
				return false;
			}
		},
		
		clickDelete : function(me){		
			if (me.beforeDeleteSave() == false){//删除保存前操作
				return;
			}

			if(me.grid.getSelectionModel().hasSelection()){
				var rows = me.grid.getSelectionModel().getSelections();
				var pkValues = "";
				
				for(var i=0;i<rows.length;i++){
					pkValues += rows[i].data[me.pk] + ",";
				}
				
				pkValues =  pkValues.substr(0,pkValues.length -1);
				
				Ext.Msg.confirm('删除', '确定删除吗?', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request({
							url : me.batchDeleteUrl,
							params : {"PKVALUES" : pkValues},
							success : function(response, opts) {	
								
								Ext.fm.msg(me.cn,"恭喜，删除成功！");
								
								me.grid.getStore().reload();
								
								me.afterDeleteSave();//删除保存后操作
							},
							failure : function(response, opts) {
								var obj = Ext.util.JSON.decode(response.responseText);
								Ext.Msg.show({
									title : '异常提示',
									msg : obj.msg,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
							}
						});
					}
				});
			}else{
				Ext.Msg.show({
					title : '友情提示',
					msg : '请选择一条记录！',
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.WARNING
				});
			}
		},
		
		clickDownload : function(me){
			var params = "&FILE_DOWNLOAD_NAME="+me.cn;
			if(me.ds.sortInfo){
				params += "&dir="+me.ds.sortInfo.direction+"&sort="+me.ds.sortInfo.field;
			}
			if(me.ds.lastOptions && me.ds.lastOptions.params && me.ds.lastOptions.params.searchField ){
				params += "&searchField="+me.ds.lastOptions.params.searchField+"&searchValue="+me.ds.lastOptions.params.searchValue;
			}
			
			var exportForm = new Ext.FormPanel();
			
	    	exportForm.applyToMarkup(Ext.DomHelper.append(Ext.getBody(), {
				tag : "div"
			}, true));
	    	
	    	var cmStr = encodeURIComponent(Ext.util.JSON.encode(me.gridObj.cmArray));  	
	    	console.log(me.gridObj.cmArray);
	    	Ext.DomHelper.append(exportForm.getForm().getEl(),"<input type=\"hidden\" name=\"FILE_DOWNLOAD_PROPERTY\" value=\""+cmStr+"\"/>");
	    	
	    	exportForm.getForm().getEl().dom.method = "post"; 
	    	exportForm.getForm().getEl().dom.action = me.gridObj.tableAction+params;  
	    	exportForm.getForm().getEl().dom.submit();
		},
		
		clickSave : function(me){			
			var url = "";
			
			if(me.opType == 'add'){//如果是添加模式，请求地址
				if (me.beforeAddSave() == false){
					return;
				}
				
				if(me.opSqlid == ''){//处理添加请求
					url = "common.action?command="+me.en + ".insert";
				}else{
					url = "common.action?command="+me.opSqlid;
				}
				
			}else if(me.opType == 'modify'){//如果是修改模式，请求地址
				if (me.beforeModifySave() == false){
					return;
				}
				
				if(me.opSqlid == ''){//处理添加请求
					url = "common.action?command="+me.en + ".update";
				}else{
					url = "common.action?command="+me.opSqlid;
				}
			}
			
			Ext.fFaceUtil.kindeditorSync(me);//对含有kindeditor控件进行处理
			
			if (me.form.getForm().isValid()) {
				me.form.getForm().submit({
					url : url,
					params : me.params,
					waitTitle : '请稍候',
					waitMsg : '正在提交表单数据,请稍候...',
					success : function(form, action) {
						if(!action.result.success){
							Ext.Msg.show({
								title : '异常提示',
								msg : action.result.msg,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
							return;
						}
						if (me.opType == 'add') {
		
							Ext.fm.msg(me.cn,"恭喜，添加成功！");
							
							me.ds.reload();//刷新列表
							
							me.addSuccess();//添加成功之后
							
							me.afterAddSave();//在添加保存之后处理
						} else {
							Ext.fm.msg(me.cn,"恭喜，修改成功！");
							
							me.ds.reload();//刷新列表
							
							me.afterModifySave();//在修改保存之后处理
						}
					},
					failure : function(form, action) {
						Ext.Msg.show({
							title : '错误提示',
							msg : action.result.msg,
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.ERROR
						});
					}
				});
			}
		},
				
		afterFormShow : function(me){
			Ext.fFaceUtil.kindeditorInit(me);
			Ext.fFaceUtil.flyingResizeInit(me);
		}
	}	
})