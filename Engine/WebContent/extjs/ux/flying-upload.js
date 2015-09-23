Ext.namespace("Ext.flying");

Ext.flying.upload = Ext.extend(Ext.Panel,{
	submitName : "fileIds",	//提交的名称
	values : [],		//包含文件ID
	hiddenInput : null,	    //隐藏域
	fileArea : null,	//文件区域
	bizId : "",
	initFile : function(bizId){
		 var me = this;
		 Ext.Ajax.request({
				url : 'common.action?command=WJ.selectByWjgl',
				params : {"WJ" : bizId},
				method : 'GET',
				success : function(response, opts) {
					var jsonObj= Ext.util.JSON.decode(response.responseText);
					
					if(jsonObj.data != null){
						for(var i =0;i<jsonObj.data.length;i++){
							var tpl = new Ext.Template(
									'<span id="span_{WJBM}">',
									'<a href="{PATH}" target="_blank">{NAME}<a/>',
									'<a  href="javascript:void(0)" onclick="Ext.flying.upload.delFile(\'{WJBM}\')"><span class="close"></span></a>',
									'<span style="width: 16px;height: 16px;vertical-align: middle;display: inline-block"></span></span>'
								);
								Ext.DomHelper.append(me.fileArea.body, tpl.apply(jsonObj.data[i]));
						}
					}
				},
				failure: function(response, opts){
					var obj= Ext.util.JSON.decode(response.responseText);
		        	Ext.Msg.show({
						title : '错误提示',
						msg : obj.msg,
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.ERROR
					});
		        }
			});
		
	},
	clear : function(){
		var spans = Ext.query("span[id^=span_]");
		
		for(var i =0;i<spans.length;i++){
			spans[i].parentNode.removeChild(spans[i]);
		}
		this.hiddenInput.value = "";
		this.values = [];
	},
	onRender : function(ct, position) {
		var me = this;
		
		Ext.flying.upload.superclass.onRender.call(this, ct, position);

		//隐藏域
		me.template =  new Ext.Template('<input type="hidden" name="{0}" value="" />');	//隐藏域

		var hiddenInput;
		var targs = [me.submitName];

		hiddenInput = me.template.append(ct, targs, false);	//false返回htmlElement
		me.hiddenInput = hiddenInput;
		
		me.uploadBtn = new Ext.Button({
			text : "上传",
			handler : function(){
				me.uploadDialog = new Ext.flying.UploadDialog({
					uploadUrl : Ext.fcache.serverPath+'UploadFileServlet',
					filePostName : 'myUpload', //这里很重要，默认值为'fileData',这里匹配action中的setMyUpload 属性
					flashUrl : 'extjs/ux/fileupload/swfupload.swf',
					fileSize : '2048 MB', 
					fileTypes : '*.*',
					fileTypesDescription : '所有文件',
					scope : me,
					onUploadSuccess : function(file, serverData){
						var jsonData = Ext.util.JSON.decode(serverData);
						if(jsonData.success){
							var tpl = new Ext.Template(
								'<span id="span_{id}">',
								'<a href="{url}" target="_blank">{fileName}<a/>',
								'<a  href="javascript:void(0)" onclick="Ext.flying.upload.delFile(\'{id}\')"><span class="close"></span></a>',
								'<span style="width: 16px;height: 16px;vertical-align: middle;display: inline-block"></span></span>'
							);
							me.values.push(jsonData.data["id"]);
							me.hiddenInput.value +=","+jsonData.data["id"];
							Ext.DomHelper.append(me.fileArea.body, tpl.apply(jsonData.data));
						}else{	//上传验证失败，给出提示信息
							Ext.Msg.show({
								title : '错误提示',
								msg : jsonData.msg,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
						}
					}
				});
				
				me.uploadDialog.show();
			}
		});
		
		me.fileArea = new Ext.Panel({
			frame :true,
			title : "文件区域",
			items : [me.uploadBtn]
		});
		
		me.add(me.fileArea);
		
		if(me.bizId != null && me.bizId != ""){
			me.initFile(me.bizId);
		}
	},
	getValue : function(){
		return this.hiddenInput.value;
	}
});
Ext.apply(Ext.flying.upload,{
	delFile : function(id) {
		Ext.Ajax.request({
			url : 'common.action?command=WJ.delete',
			params : {"WJBM" : id},
			method : 'GET',
			success : function(response, opts) {
				var obj= Ext.util.JSON.decode(response.responseText);
				
				var dom = Ext.get("span_"+id).dom;
		        dom.parentNode.removeChild(dom);
			},
			failure: function(response, opts){
				var obj= Ext.util.JSON.decode(response.responseText);
	        	Ext.Msg.show({
					title : '错误提示',
					msg : obj.msg,
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
	        }
		});
	}
}); 

Ext.reg('upload',Ext.flying.upload);