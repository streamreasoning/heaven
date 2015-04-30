#THIS SCRIPT GENERATES THE DASHBOARD PLOT FOR THE EXPERIMENTS NUMBER PASSED TO THE FUNCITON PLOT_PATH, ACCORDING WITH THE REPORT FILES
#RIGHT NOW IT PLOTS THE DASHBOARD FOR THE DIAGONAL WITH K=1, THE ROW TOTAL = 10000 AND THE COLUMN EW=10

log_plot_dash <- function(x,y,sdx,sdy, col, pch){
  
  par(new=TRUE)
  points(x = x, y = y, col=col, type = "p", pch=pch)
  # arrows(lx, log(y-sdy),lx,log(y+sdy),, length=0.05, angle=90, code=3, col="grey55")
  #arrows(log(sdx), ly,log(x+2*sdx),ly, length=0.05, angle=90, code=3, col="gray55")
}



plot_path <- function(exps, xlim, ylim){
  
  options("scipen"=5)
  
  name <- "DASHBOARD SPLIT LIN_"
  
  for(e in exps){
    
    INC_GRAPH <-e
    INC_STMT <- INC_GRAPH+1
    NAIVE_GRAPH <- INC_STMT+1
    NAIVE_STMT <- NAIVE_GRAPH+1
    
    name <- paste(name, "EXP", lat_report_rhodf$EN[INC_GRAPH], "K", lat_report_rhodf$K[INC_GRAPH] , "EW", lat_report_rhodf$EW[INC_GRAPH], "_",sep="")
  }
  
  png(paste(name, ".png", sep=""), 2000, 2000)
  
  split.screen(c(length(exps),1))
  for(i in 1:length(exps)){
    screen(i)
    
    plot(seq(0,600,10), seq(0,600,10), type = "n", ylab = "Memory", xlab = "Latency")
    
    
#     abline( h = seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.01), lty = 3, col = colors()[ 440 ] )
#     abline( v = seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.01), lty = 3, col = colors()[ 440 ] )
#     
    INC_GRAPH <- exps[i]
    INC_STMT <- INC_GRAPH+1
    NAIVE_GRAPH <- INC_STMT+1
    NAIVE_STMT <- NAIVE_GRAPH+1

    #Graph naive
    log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_GRAPH], mem_report_rhodf$A..Mean.SS[NAIVE_GRAPH], lat_report_rhodf$Dev.StdSS[NAIVE_GRAPH], mem_report_rhodf$A.Dev.Std.SS[NAIVE_GRAPH], "red", 0)
    
    #stmt naive
    log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_STMT],mem_report_rhodf$A..Mean.SS[NAIVE_STMT], lat_report_rhodf$Dev.StdSS[NAIVE_STMT], mem_report_rhodf$A.Dev.Std.SS[NAIVE_STMT], "blue", 15)
    
    #Graph inc
    log_plot_dash(lat_report_rhodf$Mean.SS[INC_GRAPH],mem_report_rhodf$A..Mean.SS[INC_GRAPH], lat_report_rhodf$Dev.StdSS[INC_GRAPH], mem_report_rhodf$A.Dev.Std.SS[INC_GRAPH], "black", 1)
    
    #stmt inc
    log_plot_dash(lat_report_rhodf$Mean.SS[INC_STMT],mem_report_rhodf$A..Mean.SS[INC_STMT], lat_report_rhodf$Dev.StdSS[INC_STMT], mem_report_rhodf$A.Dev.Std.SS[INC_STMT], "green", 16)
    
    legend(2,10,pch=c(0,15,1,16), bg = "white", legend=c("NAIVE GRAPH", "NAIVE STMT", "INC GRAPH", "INC STMT"), cex=1 , col=c("red","blue","black","green"))
    
    title(paste("EN",lat_report_rhodf$EN[NAIVE_GRAPH], lat_report_rhodf$EN[INC_STMT],"EW", lat_report_rhodf$EW[NAIVE_GRAPH], "K", lat_report_rhodf$K[NAIVE_GRAPH]),"")
    
  }
  
  dev.off()
  print("----END----")
  
}

lat_report_rhodf <- lat_report_rhodf[with(lat_report_rhodf, order(EN)),]
mem_report_rhodf <- mem_report_rhodf[with(mem_report_rhodf, order(EN)),]

# xlim <- c(0.05,5)
# ylim <- c(2.5,10)
# #EW=1
# plot_path(c(1,5,9,13), xlim, ylim)
#K*EW=10000
xlim <- c(4,5.5)
ylim <- c(4,8)
plot_path(c(33,45,53,57), xlim, ylim)
#K=1
# xlim <- c(0.05,5)
# ylim <- c(2.5,10)
# plot_path(c(1,21,37,49,57), xlim, ylim)



