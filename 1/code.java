
String HistoryDeviceName = Sys.getHostName();
String thisStationName = Sys.getStation().getStationName(); 
List<BHistoryId> lstHist = null;
BTypeSpec typespec = null;
String extName = "";


public void onStart() throws Exception
{
  // start up code here
}

public void onExecute() throws Exception
{
  BOrd ord = BOrd.make(getQueryString().trim());
  String strIn = StatusEnumToString();
  configExt(strIn);
  
  createHistory(ord);
  updateHistoryExt();
  
}


public void onStop() throws Exception
{
  // shutdown code here
}


public void createHistory(BOrd ord)
{ 
  lstHist = new ArrayList<BHistoryId>();
  BITable result = (BITable)ord.resolve().get();
  Column []columns = result.getColumns().list();
    
  Column pSlot = columns[0];
  Column pType = columns[1];
  
  TableCursor c = (TableCursor)result.cursor();

  while (c.next())
  {
    String point = BOrd.make("station:|" + c.cell(pSlot).toString()) + "";
    //println("Point: "+ point);
        
    String historyName = point.substring(point.lastIndexOf('/')).trim();
    historyName = "/" + thisStationName + historyName;
    lstHist.add(BHistoryId.make(historyName));
    
    try{
      Object obj = BOrd.make(point).resolve().get();
      if(obj instanceof BNumericWritable){
        ((BNumericWritable)obj).add(extName, new BNumericIntervalHistoryExt());
      }
      else if(obj instanceof BBooleanWritable){
        ((BBooleanWritable)obj).add(extName, new BBooleanIntervalHistoryExt());
      }
      // ....
    } 
    catch(DuplicateSlotException e){
      println("Duplicate Slot Exception in: " + point);
    }
    catch(Exception e){
      println("Exception: " + e);
    }
  }
}


private void updateHistoryExt() throws Exception {  
   int idx = getQueryString().lastIndexOf('|');
   String qrString = getQueryString().substring(0,idx).trim() + "|bql:select * from history:HistoryExt";
   BOrd ord = BOrd.make(qrString);
   BITable result = (BITable)ord.resolve().get();
   try(TableCursor c = (TableCursor)result.cursor()){
      while(c.next()){
        BHistoryExt jg = (BHistoryExt)c.get();
        BHistoryConfig jg2 = jg.getHistoryConfig();
        
        jg.setEnabled(false);
        
        //jg2.setRecordType(typespec);
        jg2.setCapacity(getHistoryConfig().getCapacity());
        jg2.setInterval(BCollectionInterval.make(getInterval()));
        
        jg.setEnabled(true);
      }
   }
}
  
  
public void configExt(String in){
  if(in.equals("NumericInterval")){
     extName = "NumericInterval";
     //trendRecord = "NumericTrendRecord";
     //typespec = BTypeSpec.make(new BNumericTrendRecord().getType());
  }
  else if(in.equals("BooleanInterval")){
     extName = "BooleanInterval";
    
     //typespec = BTypeSpec.make(new BBooleanTrendRecord().getType());
  }
  //...
}

public String StatusEnumToString() throws Exception{
  String result = "";
  try
   {
      BLink[] links = getTypeHist().getParentComponent().getLinks(); //getLinks(getType());
      for (int i = 0; i < links.length; i++) 
      {
         if ((links[i].getSourceComponent().getType() != BEnumWritable.TYPE)) 
         {
          continue;
         }
         BEnumWritable sc  = ((BEnumWritable) links[i].getSourceComponent());
         BEnumRange  range = ((BEnumRange) sc.getFacets().get("range"));
         result =  range.getDisplayTag(getTypeHist().getValue().getOrdinal(), null);
      }
   }
   catch (Exception e) 
   {
     println(" getMessage = " + e.getMessage() + "\n getStackTrace = " 
     + e.getStackTrace() + "\n toString = " + e.toString());  
   }
   return result;
} 

  


