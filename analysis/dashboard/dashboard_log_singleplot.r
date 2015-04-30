#THIS SCRIPT GENERATES THE DASHABORD FOR EACH EXPERIMENT IN A DEDICATED PLOT,  WITH THE FOUR BASELINES IN A SINGLE PLOT, LOG SCALE

log_plot_dash <- function(x,y,sdx,sdy, col, pch){
  if(x>1 && y >1){
    print(paste(col, "lat", x,"sd", sdx))
    print(paste(col, "mem", y, "sd", sdy))
    lx <- log(x)
    ly <- log(y)
    
    
    points(x = lx, y = ly, col=col, type = "p", pch=pch)
    arrows(lx, log(y-sdy),lx,log(y+sdy),, length=0.05, angle=90, code=3, col="grey55")
    arrows(log(x-sdx), ly,log(x+sdx),ly, length=0.05, angle=90, code=3, col="gray55")
    
  }
}

plot_dashboard <- function(i){
  print(paste("----START",i, "----"))
  NAIVE_GRAPH <- i
  NAIVE_STMT <- NAIVE_GRAPH+1
  INC_GRAPH <- NAIVE_STMT+1
  INC_STMT <- INC_GRAPH+1
  
  plot(seq(0.025,200,0.1), seq(0.025,50,0.025), log="xy", type = "n", ylab = "Memory", xlab = "Latency")
  abline( h = seq( 0.1, 200, 0.05 ), lty = 3, col = colors()[ 440 ] )
  abline( v = seq( 0.1, 200, 0.05), lty = 3, col = colors()[ 440 ] )
  
  
  #Graph naive
  log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_GRAPH], mem_report_rhodf$A..Mean.SS[NAIVE_GRAPH], lat_report_rhodf$Dev.StdSS[NAIVE_GRAPH], mem_report_rhodf$A.Dev.Std.SS[NAIVE_GRAPH], "red", 0)
  
  #stmt naive
  log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_STMT],mem_report_rhodf$A..Mean.SS[NAIVE_STMT], lat_report_rhodf$Dev.StdSS[NAIVE_STMT], mem_report_rhodf$A.Dev.Std.SS[NAIVE_STMT], "blue", 15)
  
  #Graph inc
  log_plot_dash(lat_report_rhodf$Mean.SS[INC_GRAPH],mem_report_rhodf$A..Mean.SS[INC_GRAPH], lat_report_rhodf$Dev.StdSS[INC_GRAPH], mem_report_rhodf$A.Dev.Std.SS[INC_GRAPH], "black", 1)
  
  #stmt inc
  log_plot_dash(lat_report_rhodf$Mean.SS[INC_STMT],mem_report_rhodf$A..Mean.SS[INC_STMT], lat_report_rhodf$Dev.StdSS[INC_STMT], mem_report_rhodf$A.Dev.Std.SS[INC_STMT], "green", 16)
  
#   abline(v = log(min(lat_report_rhodf$Mean.SS[NAIVE_GRAPH],lat_report_rhodf$Mean.SS[NAIVE_STMT],lat_report_rhodf$Mean.SS[INC_GRAPH],lat_report_rhodf$Mean.SS[INC_STMT])),  col = "black")
#   abline (v = log(max(lat_report_rhodf$Mean.SS[NAIVE_GRAPH],lat_report_rhodf$Mean.SS[NAIVE_STMT],lat_report_rhodf$Mean.SS[INC_GRAPH],lat_report_rhodf$Mean.SS[INC_STMT])), col = "black")
  
  legend(300,100,pch=c(0,15,1,16), bg = "white", legend=c("NAIVE GRAPH", "NAIVE STMT", "INC GRAPH", "INC STMT"), cex=0.9 , col=c("red","blue","black","green"))
  title(paste("TRIPLE", mem_report_rhodf$K[NAIVE_GRAPH], "SLOT", mem_report_rhodf$EW[NAIVE_GRAPH]), paste("EXP", lat_report_rhodf$EN[NAIVE_GRAPH], lat_report_rhodf$EN[INC_STMT]))
  
  print("----END----")
}

for(i in seq(1,60,4)){
  
  if(i!=17){
    png(paste(paste("DASHBOARD LOG EXP", lat_report_rhodf$EN[i], lat_report_rhodf$EN[i+2]), "TRIPLE", mem_report_rhodf$K[i], "SLOT", mem_report_rhodf$EW[i], ".png", sep="_"), 1000, 1000)
    plot_dashboard(i)
    dev.off()
  }
  
  
}



