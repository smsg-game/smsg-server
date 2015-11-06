
Ext.onReady(function(){

    var match_type_store = new Ext.data.SimpleStore({
                       fields: ['match_type', 'match_type_name'],
                       data: [
                          ['5', '联业联赛'],
                          ['1', '职业友谊'],
                          ['6', '胜者为王'],
                       ]
                             
    });
    
    var match_type_combobox = new Ext.form.ComboBox({
                 id: 'match_type_combobox',
                 store: match_type_store,
                 mode: 'local',
                 displayField:'match_type_name',
                 valueField:'match_type',
                 triggerAction: 'all',
                 forceSelection: true,
                 width:100,
                 value: '5',
                 listeners: {
                   "select": function(){
                      store.load();
                   }
                 }
     });
    
	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'home_team_id'},
		{name: 'guest_team_id'},
		{name: 'status'},
		{name: 'show_status'},
		{name: 'next_show_status'},
		{name: 'expired_time'},
		{name: 'point'},
		{name: 'type'},
		{name: 'overtime'},
		{name: 'created_time'},
		{name: 'start_time'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/match_list_json/'}),
        						    reader: data_read,
        							remoteSort: true ,
        					       });
       
    store.on('beforeload', function() {  
       var match_type = match_type_combobox.getValue() ;
       this.baseParams = {  
         'match_type': match_type,
       };  
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
            width: 55,
        },{
            id: 'home_team_id',
            header: '主队id',
            dataIndex: 'home_team_id',
            width: 80,
        },{
            id: 'guest_team_id',
            header: '客队id',
            dataIndex: 'guest_team_id',
            width: 80,
        },{
            id: 'type',
            header: '比赛类型',
            dataIndex: 'type',
            width: 80,
            renderer: function(value){
                var html;
                switch(value){
                 case 1:
                  html = "<font color=\"green\">职业友谊 </font>";
                  break;
                 case 5:
                  html = "<font color=\"blue\">职业联赛</font>";
                  break;
                 case 6:
                  html = "<font color=\"red\">胜者为王 </font>";
                  break; 
                 default:
                  html = "未知类型";
                  break;
                }
                return html;
            }
        },{
            id: 'created_time',
            header: '创建时间',
            dataIndex: 'created_time',
            width: 150,
        },{
            id: 'start_time',
            header: '开始时间',
            dataIndex: 'start_time',
            width: 150,
        },{
            id: 'expired_time',
            header: '超时时间',
            dataIndex: 'expired_time',
            width: 150,
        },{
            id: 'status',
            header: '状态',
            dataIndex: 'status',
            width: 60,
        },{
            id: 'show_status',
            header: '显示状态',
            dataIndex: 'show_status',
            width: 60,
        },{
            id: 'next_show_status',
            header: '下一显示状态',
            dataIndex: 'next_show_status',
            width: 60,
        },{
            id: 'point',
            header: '比分',
            dataIndex: 'point',
            width: 80,
        },{
            id: 'overtime',
            header: '加时次数',
            dataIndex: 'overtime',
            width: 40,
        }],
        tbar: [
            match_type_combobox,
        ],
        bbar: new Ext.PagingToolbar({
	        pageSize: 20,
	        store: store,
	        displayInfo: true,
	        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
	        emptyMsg: "没有记录"
	    })
      
    });
    var downurl_panel = new Ext.Panel({
        title: '比赛管理',
        layout: 'border',
        renderTo: 'data_div',
        layoutConfig: {
            columns: 1
        },
        height:650,
        items: [data_grid_panel]
    });
    
    store.load();
    
});