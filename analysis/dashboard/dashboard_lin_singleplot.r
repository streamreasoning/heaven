#THIS SCRIPT GENERATES THE PLOT OF THE LINEAR DASHBOARD, GATHERD FOR EACH CELL IN THE EXPERIMENT RAPPRESENTATION WHERE THE ROWS REPORT TOTAL TRIPLE IN WINDOW AND COLUMN EW
#IT LOOP OVER ALL EXPERIMENT ACCORDING TO THE REPORT FILE WHERE LINES ARE SORTED BY EN (NAIVE AND INCREMENTAL)


for(i in seq(1,60,4)){
  
  NAIVE_GRAPH <- i
  NAIVE_STMT <- NAIVE_GRAPH+1
  INC_GRAPH <- NAIVE_STMT+1
  INC_STMT <- INC_GRAPH+1
  
  png(paste(paste("EXP", lat_report_rhodf$EN[NAIVE_GRAPH], lat_report_rhodf$EN[INC_STMT]), "TRIPLE", mem_report_rhodf$K[NAIVE_GRAPH], "SLOT", mem_report_rhodf$EW[NAIVE_GRAPH], ".png", sep="_"), 1000, 1000)
  
  
  plot(seq(0,600,10), seq(0,600,10), type = "n", ylab = "Memory", xlab = "Latency")
  legend(300,100,pch=c(0,15,1,16), bg = "white", legend=c("NAIVE GRAPH", "NAIVE STMT", "INC GRAPH", "INC STMT"), cex=0.9 , col=c("red","blue","black","green"))
  title(paste("TRIPLE", mem_report_rhodf$K[NAIVE_GRAPH], "SLOT", mem_report_rhodf$EW[NAIVE_GRAPH]), paste("EXP", lat_report_rhodf$EN[NAIVE_GRAPH], lat_report_rhodf$EN[INC_STMT]))
  abline( h = seq( 0, 600, 10 ), lty = 3, col = colors()[ 440 ] )
  abline( v = seq( 0, 600, 10 ), lty = 3, col = colors()[ 440 ] )
  
  
  #Graph naive
  x <- lat_report_rhodf$Median.SS[NAIVE_GRAPH]
  y <- mem_report_rhodf$A..Median.SS[NAIVE_GRAPH]
  par(new=TRUE)
  points(x = x, y = y, col="red", type = "p", pch=0)
  
  #stmt naive
  x <- lat_report_rhodf$Median.SS[NAIVE_STMT]
  y <- mem_report_rhodf$A..Median.SS[NAIVE_STMT]
  
  
  par(new=TRUE)
  points(x = x, y = y, col="blue", type = "p", pch=15)
  
  #Graph inc
  x <- lat_report_rhodf$Median.SS[INC_GRAPH]
  y <- mem_report_rhodf$A..Median.SS[INC_GRAPH]
  
  
  par(new=TRUE)
  points(x = x, y = y, col="black", type = "p", pch=1)
  
  
  #stmt inc
  x <- lat_report_rhodf$Median.SS[INC_STMT]
  y <- mem_report_rhodf$A..Median.SS[INC_STMT]
  
  par(new=TRUE)
  points(x = x, y = y, col="green", type = "p", pch=16)
  
 dev.off()
  
}




