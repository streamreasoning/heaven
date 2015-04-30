#THIS SCRIPT GENERATES THE DASHBOARD PLOT FOR THE EXPERIMENTS NUMBER PASSED TO THE FUNCITON PLOT_PATH, ACCORDING WITH THE REPORT FILES
#RIGHT NOW IT PLOTS THE DASHBOARD FOR THE DIAGONAL WITH K=1, THE ROW TOTAL = 10000 AND THE COLUMN EW=10
log_plot_dash <- function(x,y,sdx,sdy, col, pch){
 if(x>1 && y >1){
#  print(paste(col, "lat", x,"sd", sdx))
#  print(paste(col, "mem", y, "sd", sdy))
  lx <- log(x)
  ly <- log(y)
 
  
  points(x = lx, y = ly, col=col, type = "p", pch=pch)
  #arrows(lx, log(y-sdy),lx,log(y+sdy),, length=0.05, angle=90, code=3, col="grey55")
  #arrows(log(x-sdx), ly,log(x+sdx),ly, length=0.05, angle=90, code=3, col="gray55")
  
  }
}

plot_path <- function(exps, xlim, ylim){
  
  options("scipen"=5)
  
  name <- "PAPER DASHBOARD LOG_"

  
  for(e in exps){
    
    INC_GRAPH <-e
    INC_STMT <- INC_GRAPH+1
    NAIVE_GRAPH <- INC_STMT+1
    NAIVE_STMT <- NAIVE_GRAPH+1

    
    name <- paste(name, "EXP", lat_report_rhodf$EN[INC_GRAPH], "K", lat_report_rhodf$K[INC_GRAPH] , "EW", lat_report_rhodf$EW[INC_GRAPH], "_",sep="")
  }

  #png(paste(name, ".png", sep=""), 1200, 1200)
  postscript(paste(name,".eps", sep=""), onefile=TRUE, paper="a4") #inches
  
  plot(seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.001),seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.001), cex.lab=1.5, log="xy",  type = "n", ylab = "Memory (mb)", xlab = "Latency (ms)", xlim=xlim, ylim=ylim)
  
  abline( h = seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.1), lty = 3, col = colors()[ 440 ] )
  abline( v = seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.05), lty = 3, col = colors()[ 440 ] )
  
  for(e in exps){
    
    INC_GRAPH <-e
    INC_STMT <- INC_GRAPH+1
    NAIVE_GRAPH <- INC_STMT+1
    NAIVE_STMT <- NAIVE_GRAPH+1
    
    #Graph naive
    pch <- 0
    color <- "red"
    colors <- color
    pchs <- pch
    legend <- paste(lat_report_rhodf$REASONING[NAIVE_GRAPH], lat_report_rhodf$STREAM[NAIVE_GRAPH])
    
    log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_GRAPH], mem_report_rhodf$A..Mean.SS[NAIVE_GRAPH], lat_report_rhodf$Dev.StdSS[NAIVE_GRAPH], mem_report_rhodf$A.Dev.Std.SS[NAIVE_GRAPH], color, pch)
  
    #stmt naive
    pch <- 15
    color <- "blue"
    colors <- c(colors, color)
    pchs <- c(pchs, pch)
    legend <- c(legend, paste(lat_report_rhodf$REASONING[NAIVE_STMT], lat_report_rhodf$STREAM[NAIVE_STMT]))
    
    
    log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_STMT],mem_report_rhodf$A..Mean.SS[NAIVE_STMT], lat_report_rhodf$Dev.StdSS[NAIVE_STMT], mem_report_rhodf$A.Dev.Std.SS[NAIVE_STMT], color, pch)
    #Graph inc
    
    pch <- 1
    color <- "black"
    colors <- c(colors, color)
    pchs <- c(pchs, pch)
    legend <- c(legend, paste(lat_report_rhodf$REASONING[INC_GRAPH], lat_report_rhodf$STREAM[INC_GRAPH]))
    
    
    log_plot_dash(lat_report_rhodf$Mean.SS[INC_GRAPH],mem_report_rhodf$A..Mean.SS[INC_GRAPH], lat_report_rhodf$Dev.StdSS[INC_GRAPH], mem_report_rhodf$A.Dev.Std.SS[INC_GRAPH], color, pch)
    #stmt inc
    pch <- 16
    color <- "green"
    colors <- c(colors, color)
    pchs <- c(pchs, pch)
    legend <- c(legend, paste(lat_report_rhodf$REASONING[INC_STMT], lat_report_rhodf$STREAM[INC_STMT]))
    
    log_plot_dash(lat_report_rhodf$Mean.SS[INC_STMT],mem_report_rhodf$A..Mean.SS[INC_STMT], lat_report_rhodf$Dev.StdSS[INC_STMT], mem_report_rhodf$A.Dev.Std.SS[INC_STMT], color, pch)
  }
  
  legend(0.5,8,pch=pchs, bg = "white", legend=legend, cex=0.9 , col=colors)
  
  
  print("----END----")
  dev.off()

}

# for(i in seq(1,60,4)){
#   
#   if(i!=17){
#     #png(paste(paste("EXP", lat_report_rhodf$EN[i], lat_report_rhodf$EN[i+2]), "TRIPLE", mem_report_rhodf$K[i], "SLOT", mem_report_rhodf$EW[i], ".png", sep="_"), 1000, 1000)
#     #plot_dashboard(i)
#     #dev.off()
#   }
#   
#   
# }

lat_report_rhodf <- lat_report_rhodf[with(lat_report_rhodf, order(EN)),]
mem_report_rhodf <- mem_report_rhodf[with(mem_report_rhodf, order(EN)),]


# xlim <- c(0.05,5)
# ylim <- c(2.5,10)
# #EW=1
# plot_path(c(1,5,9,13), xlim, ylim)
# 
# xlim <- c(0.05,5)
# ylim <- c(2.5,10)
# #EW=10
#plot_path(c(1,5,9,13), xlim, ylim)
# plot_path(c(21,25,29,33), xlim, ylim)
# #K*EW=10000
# xlim <- c(4,5.5)
# ylim <- c(4,8)
# plot_path(c(33,45,53,57), xlim, ylim)
K=1
xlim <- c(0.05,5)
ylim <- c(2.5,8)
plot_path(c(21,37,49,57), xlim, ylim)



