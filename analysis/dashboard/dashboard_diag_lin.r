#THIS SCRIPT GENERATES THE PLOT FOR THE DIAGONAL K=1 IN THE CONFIGURATION TOTAL_TRIPLE_IN_WINDOW/EW PLOTTING THE FOUR BASELINES IN THE SAME FIGURE IN 4 DIFFERENT PLOTS
#IT USES LINEAR SCALING

plot_dash <- function(x,y,sdx,sdy, col, pch){
  
  par(new=TRUE)
  points(x = x, y = y, col=col, type = "p", pch=pch)
  arrows(x, y-sdy,x,y+sdy, length=0.05, angle=90, code=3)
  arrows(x-sdx, y,x+sdx,y, length=0.05, angle=90, code=3)
  
}

dashboard <- function(a,b,c,d,e, ss = c(FALSE, FALSE, FALSE, FALSE, FALSE)){
  
  exp <- paste(lat_report_rhodf$STREAM[a], substr(lat_report_rhodf$REASONING[a], 0, 6))
  
   exp <- paste(lat_report_rhodf$STREAM[a], substr(lat_report_rhodf$REASONING[a], 0, 6))
  
		plot(seq(0,200,4),seq(0,650,13),  type = "n", ylab = "Memory", xlab = "Latency")
  abline( h = seq( 0, 800, 10 ), lty = 3, col = colors()[ 440 ] )
		abline( v = seq( 0, 800, 10 ), lty = 3, col = colors()[ 440 ] )
  
  z<-a
  if(ss[1]){
    pch <- 1
  }else{
    pch <- 15
  }
  pch_legend <- pch
  plot_dash(lat_report_rhodf$Mean.SS[z], mem_report_rhodf$A..Mean.SS[z], lat_report_rhodf$Dev.StdSS[z],mem_report_rhodf$A.Dev.Std.SS[z], "red", pch)
  
  z<-b
  if(ss[2]){
    pch <- 1
  }else{
    pch <- 16
  }
  pch_legend <- c(pch_legend,pch)
  plot_dash(lat_report_rhodf$Mean.SS[z], mem_report_rhodf$A..Mean.SS[z], lat_report_rhodf$Dev.StdSS[z], mem_report_rhodf$A.Dev.Std.SS[z], "blue", pch)
 
  
  z<-c
  if(ss[3]){
    pch <- 2
  }else{
    pch <- 17
  }
  pch_legend <- c(pch_legend,pch)
  plot_dash(lat_report_rhodf$Mean.SS[z], mem_report_rhodf$A..Mean.SS[z], lat_report_rhodf$Dev.StdSS[z], mem_report_rhodf$A.Dev.Std.SS[z], "black", pch)
  
  z <- d
  if(ss[4]){
    pch <- 5
  }else{
    pch <- 18
  }
  pch_legend <- c(pch_legend,pch)
  plot_dash(lat_report_rhodf$Mean.SS[z], mem_report_rhodf$A..Mean.SS[z], lat_report_rhodf$Dev.StdSS[z], mem_report_rhodf$A.Dev.Std.SS[z], "chartreuse4", pch)
  
  z <-e
  if(ss[5]){
    pch <- 4
  }else{
    pch <- 8
  }
  pch_legend <- c(pch_legend,pch)
  plot_dash(lat_report_rhodf$Mean.SS[z], mem_report_rhodf$A..Mean.SS[z], lat_report_rhodf$Dev.StdSS[z], mem_report_rhodf$A.Dev.Std.SS[z], "purple", pch)
  
  legend(x=0,y=600,pch=pch_legend,bg = "white",bty="o", legend=c(
    paste( "K", lat_report_rhodf$K[a], "EW", lat_report_rhodf$EW[a]), 
    paste( "K", lat_report_rhodf$K[b], "EW", lat_report_rhodf$EW[b]), 
    paste( "K", lat_report_rhodf$K[c], "EW", lat_report_rhodf$EW[c]), 
    paste( "K", lat_report_rhodf$K[d], "EW", lat_report_rhodf$EW[d]), 
    paste( "K", lat_report_rhodf$K[e], "EW", lat_report_rhodf$EW[e])), cex=1 ,y.intersp=1, col=c("red","blue","black","chartreuse4", "purple"))
  title(exp, paste("ENs",  lat_report_rhodf$EN[a], lat_report_rhodf$EN[b], lat_report_rhodf$EN[c], lat_report_rhodf$EN[d], lat_report_rhodf$EN[e]))
  
}

png("DASHBOARD K=1 EW=X LIN DIAGONAL.png", 1000, 1000)



a <- 1
b <- 21
c <- 37
d <- 49
e <- 57
split.screen(c(2,1))
split.screen(c(1,2), screen=1)
screen(3)
#NAIVE GRAPH
dashboard(a,b,c,d,e)
screen(4)
#NAIVE STMT
dashboard(a+1,b+1,c+1,d+1,e+1)
split.screen(c(1,2), screen=2)
screen(5)
#INC GRAPH
dashboard(a+2,b+2,c+2,d+2,e+2)
screen(6)
#INC STMT
dashboard(a+3,b+3,c+3,d+3,e+3)
dev.off()
