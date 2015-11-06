
Ext.onReady(function(){

    var action_name_store = new Ext.data.Store({     
						     proxy: new Ext.data.HttpProxy({     
						         url: '/admin/action_name_list_json/' 
						     }),
						     reader: new Ext.data.JsonReader({     
						     	root: 'infos',     
						     	id: 'action_name'    
						     },[     
						         {name: 'action_name', mapping: 'action_name'},   
						         {name: 'action_name_cn', mapping: 'action_name_cn'},    
						     ])     
						 });
						 
    action_name_store.load();
    
	var action_name_combobox = new Ext.form.ComboBox({
                 id: 'action_name_combobox',
                 store: action_name_store,
                 displayField:'action_name_cn',
                 valueField:'action_name',
                 triggerAction: 'all',
                 forceSelection: true,
                 listeners: {
                   "select": function(){
                      var action_name = this.getValue();
                      store.load({
                         params: {
                            start: 0,
                            limit: 20,
                            action_name: action_name,
                         }
                      });
                   },
                   "afterrender": function(comb){
                      comb.setValue("ShortShoot");
                      comb.setRawValue("中距离投篮");
                   }
                 }
     });
    
	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'action_name'},
		{name: 'action_desc'},
		{name: 'result'},
		{name: 'flg'},
		{name: 'is_assist'},
		{name: 'not_stick'},
		{name: 'percent'},
		{name: 'created_time'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/action_desc_json/'}),
        						    reader: data_read,
        							remoteSort: true ,
        					       });
        							         
    var data_grid_panel = new Ext.grid.GridPanel({
        store: store,
        width: 600,
        height: 645,
        region:'center',
        loadMask: true,
        columns: [
           new Ext.grid.RowNumberer()
        ,{
            id: 'id',
            header: '编号',
            dataIndex: 'id',
            hidden: true,
            width: 55,
        },{
            id: 'action_name',
            header: '动作名称',
            dataIndex: 'action_name',
            width: 80,
        },{
            id: 'action_desc',
            header: '动作描述',
            dataIndex: 'action_desc',
            width: 750,
            renderer: function(value){
                return "<span title=\"" + value + "\">" + value + "</span>";
            }
        },{
            id: 'is_assist',
            header: '助攻',
            dataIndex: 'is_assist',
            width: 50,
            renderer: function(value){
              if(value==1){
                return "<font color=\"green\">是</font>";
              }else{
                return "<font color=\"red\">否</font>";
              }
            }
        },{
            id: 'result',
            header: '成功',
            dataIndex: 'result',
            width:60,
            renderer: function(value){
              if(value=="success"){
                return "<font color=\"green\">是</font>";
              }else if(value=="failure"){
                return "<font color=\"black\">否</font>";
              }else{
                return "<font color=\"red\">异常</font>";
              }
            }
        },{
            id: 'flg',
            header: '标志',
            dataIndex: 'flg',
            width: 80,
        },{
            id: 'not_stick',
            header: '三不沾',
            dataIndex: 'not_stick',
            width: 50,
            renderer: function(value){
              if(value==1){
                return "<font color=\"green\">是</font>";
              }else{
                return "<font color=\"red\">否</font>";
              }
            }
        },{
            id: 'percent',
            header: '百分比',
            align: 'right',
            dataIndex: 'percent',
            width: 50,
        },{
            id: 'edit',
            align: 'center',
            header: '编辑',
            dataIndex: 'id',
            width: 100,
            renderer: function(value){
              var href = "<a href=\"javascript:void(0);\" onclick=\"javascript:edit('" + value + "');\">edit</a>";
              href += "&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"javascript:copy('" + value + "');\">copy</a>";
              return href;
            }
        }],
        tbar: [
          action_name_combobox,
          {
        	ref: '../addBtn',
            iconCls: 'task-add',
            text: '新增描述',
            handler: function(){
			    add_action_desc();
            }
        }],
        bbar: new Ext.PagingToolbar({
	        pageSize: 20,
	        store: store,
	        displayInfo: true,
	        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
	        emptyMsg: "没有记录"
	    })
      
    });
    var downurl_panel = new Ext.Panel({
        title: '比赛描述',
        layout: 'border',
        renderTo: 'data_div',
        layoutConfig: {
            columns: 1
        },
        height:650,
        items: [data_grid_panel]
    });
    
    store.load();
    
    edit = function(id){
      edit_or_copy(id, 'edit');
    };
    
    copy = function(id){
      edit_or_copy(id, 'copy');
    };
    
    edit_or_copy = function(id, type){
      
       Ext.Ajax.request({ 
			url : '/admin/get_action_desc_json/',
			sync : true,
			params : { 
			  id: id,
			},
		    success : function(response) { 
		        var data = response.responseText;
			    var info = Ext.util.JSON.decode(data);
			    if(type=='copy'){
			        info.id = '';
			    }
			    show_edit_window(info);
			}, 
		    failure : function(response){ 
			    Ext.Msg.show({ 
			      title : '请求出错', 
			      msg : '读取描述信息出错!', 
			      icon : Ext.Msg.ERROR, 
			      buttons : Ext.Msg.OK 
			    })
			}
	    }) 
    };
    
   add_action_desc = function(){
       show_edit_window(null)
   };
    
   show_edit_window = function(info){
    	    	
    	var edit_form = new Ext.FormPanel({
	        labelWidth: 65, 
	        url: '/admin/action_desc_update/',
	        width: 650,
	        height: 340,
	        frame:true,
	        defaultType: 'textfield',
	        items: [{
	             xtype: 'hidden',
		         name: 'id',
		         value: info!=null?info.id:'',
	           },{
	             fieldLabel: '动作名称',
		         name: 'action_name',
		         value: info!=null?info.action_name:'',
	           },{
	             fieldLabel: '动作标志',
		         name: 'flg',
		         value: info!=null?info.flg:'',
	           },{
	             fieldLabel: '动作结果',
		         name: 'result',
		         value: info!=null?info.result:'',
	           },{
	             fieldLabel: '百分比',
		         name: 'percent',
		         value: info!=null?info.percent:'',
	           },{
	             xtype: 'checkbox',
	             fieldLabel: '助功',
	             style: 'margin-top:6px;',
		         name: 'is_assist',
		         checked: info!=null?info.is_assist==1:false,
	           },{
	             xtype: 'checkbox',
	             style: 'margin-top:6px;',
	             fieldLabel: '三不沾',
		         name: 'not_stick',
		         checked: info!=null?info.not_stick==1:false,
	           },{
	             xtype: 'textarea',
                 fieldLabel: '描述',
                 width: 540,
                 height: 120,
                 name: 'action_desc',
                 value: info!=null?info.action_desc:'',
               }
	        ],
	        buttons: [{
	            text: '保存',
	            handler: function(){
	               edit_form.form.submit({
	                 waitMsg: '保存中...',
	                 success: function(re, v){
	                 	 edit_form.form.reset();
	                 	 edit_window.close();
	                 	 Ext.Msg.show({ 
					       itle : '保存成功', 
					       msg : '动作描述保存成功', 
					       icon : Ext.Msg.SUCCESS, 
					       buttons : Ext.Msg.OK 
					     })
					     store.load();
	                 },
	                 failure: function(){
	                 	 Ext.Msg.show({ 
					       itle : '保存失败', 
					       msg : '动作描述保存失败', 
					       icon : Ext.Msg.ERROR, 
					       buttons : Ext.Msg.OK 
					     })
	                 }
	               });
	            }
	        },{
	            text: '取消',
	            handler: function(){
	               edit_window.close();
	            }
	        }],
	        listeners: {
               'show':function(){
                  this.center(); //屏幕居中
               }
             }
	    });
	    
	    var edit_window = new Ext.Window({  
            title: '比赛描述编辑',  
            width: 650,  
            height: 370,  
            closable: true,  
            closeAction: 'close',  
            resizable: false,   
            items: edit_form
        });  

        edit_window.show(); 
   };
});