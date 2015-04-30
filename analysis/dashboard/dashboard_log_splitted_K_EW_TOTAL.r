#THIS SCRIPT GENERATES THE DASHBOARD PLOT FOR THE EXPERIMENTS NUMBER PASSED TO THE FUNCITON PLOT_PATH, ACCORDING WITH THE REPORT FILES
#RIGHT NOW IT PLOTS THE DASHBOARD FOR THE DIAGONAL WITH K=1, THE ROW TOTAL = 10000 AND THE COLUMN EW=10

log_plot_dash <- function(x,y,sdx,sdy, col, pch){
  lx <- log(x)
  ly <- log(y)
  
 
  points(x = lx, y = ly, col=col, type = "p", pch=pch)
  par(new=TRUE)
 # arrows(lx, log(y-sdy),lx,log(y+sdy),, length=0.05, angle=90, code=3, col="grey55")
  #arrows(log(sdx), ly,log(x+2*sdx),ly, length=0.05, angle=90, code=3, col="gray55")
}


plot_path <- function(exps, xlim, ylim){
  
  options("scipen"=5)
  lettere <- c("a","b","c","d","e")
  name <- "PAPER DASHBOARD SPLIT LOG_"
  
  for(e in exps){
    
    INC_GRAPH <-e
    INC_STMT <- INC_GRAPH+1
    NAIVE_GRAPH <- INC_STMT+1
    NAIVE_STMT <- NAIVE_GRAPH+1
    
    name <- paste(name, "EXP", lat_report_rhodf$EN[INC_GRAPH], "K", lat_report_rhodf$K[INC_GRAPH] , "EW", lat_report_rhodf$EW[INC_GRAPH], "_",sep="")
  }
  
  #png(paste(name, ".png", sep=""), 1200, 1200)

  setEPS()
 
  split.screen(c(length(exps),1))
    for(i in 1:(length(exps)+1)){
      
      postscript(paste("D.",lettere[i], name,".eps", sep=""), width = 9, height = 3, onefile=TRUE) #inches
      
      
      if(i>length(exps)){
#         png(paste("LEGEND K=1 .png", sep=""), 1200, 1200)
#         screen(1)
#         plot(seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.001),seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.001), cex.lab=1.5, log="xy",  type = "n", ylab = "Memory", xlab = "Latency", xlim=xlim, ylim=ylim)
#         legend(x=0.3,y=5,pch=pchs, bg = "white", legend=legend, cex=3 , col=colors)
#         dev.off()
      }else{
        
        screen(i)
        par(mar=c(4,6.3,4,10))
        plot(seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.001),seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.001), log="xy",  type = "n", ylab = "", xlab = "", xlim=xlim, ylim=ylim)
        
        abline( h = seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.5), lty = 3, col = colors()[ 440 ] )
        abline( v = seq(min(xlim[1], ylim[1]),max(xlim[2], ylim[2]),0.5), lty = 3, col = colors()[ 440 ] )
        
        INC_GRAPH <- exps[i]
        INC_STMT <- INC_GRAPH+1
        NAIVE_GRAPH <- INC_STMT+1
        NAIVE_STMT <- NAIVE_GRAPH+1
        
        #Graph naive
        pch <- 0
        color <- "red"
        colors <- color
        pchs <- pch
        legend <- paste("GN", lat_report_rhodf$REASONING[NAIVE_GRAPH], lat_report_rhodf$STREAM[NAIVE_GRAPH])
        log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_GRAPH], mem_report_rhodf$A..Mean.SS[NAIVE_GRAPH], lat_report_rhodf$Dev.StdSS[NAIVE_GRAPH], mem_report_rhodf$A.Dev.Std.SS[NAIVE_GRAPH], color, pch)
        
        
        #Graph inc
        pch <- 1
        color <- "black"
        colors <- c(colors, color)
        pchs <- c(pchs, pch)
        legend <- c(legend, paste("GI",lat_report_rhodf$REASONING[INC_GRAPH], lat_report_rhodf$STREAM[INC_GRAPH]))
        log_plot_dash(lat_report_rhodf$Mean.SS[INC_GRAPH],mem_report_rhodf$A..Mean.SS[INC_GRAPH], lat_report_rhodf$Dev.StdSS[INC_GRAPH], mem_report_rhodf$A.Dev.Std.SS[INC_GRAPH], color, pch)
        
        
        #stmt naive
        pch <- 15
        color <- "blue"
        colors <- c(colors, color)
        pchs <- c(pchs, pch)
        legend <- c( legend, paste("TN",lat_report_rhodf$REASONING[NAIVE_STMT], "TRIPLE"))
        log_plot_dash(lat_report_rhodf$Mean.SS[NAIVE_STMT],mem_report_rhodf$A..Mean.SS[NAIVE_STMT], lat_report_rhodf$Dev.StdSS[NAIVE_STMT], mem_report_rhodf$A.Dev.Std.SS[NAIVE_STMT], color, pch)
        
        
        
        #stmt inc
        pch <- 16
        color <- "green"
        colors <- c(colors, color)
        pchs <- c(pchs, pch)
        legend <- c( legend, paste("TI",lat_report_rhodf$REASONING[INC_STMT], "TRIPLE"))
        
        log_plot_dash(lat_report_rhodf$Mean.SS[INC_STMT],mem_report_rhodf$A..Mean.SS[INC_STMT], lat_report_rhodf$Dev.StdSS[INC_STMT], mem_report_rhodf$A.Dev.Std.SS[INC_STMT], color, pch)
        
        if(lat_report_rhodf$EN[INC_GRAPH]=="8" ){
          title(paste(paste("D.",lettere[i]),"EN",lat_report_rhodf$EN[INC_GRAPH],"EW", lat_report_rhodf$EW[NAIVE_GRAPH], "K", lat_report_rhodf$K[NAIVE_GRAPH]),"NOT STEADY FOR B1 B2 B3 B4 MEMORY")
        }
        else if(lat_report_rhodf$EN[INC_GRAPH]=="10" ){
          title(paste(paste("D.",lettere[i]),"EN",lat_report_rhodf$EN[INC_GRAPH],"EW", lat_report_rhodf$EW[NAIVE_GRAPH], "K", lat_report_rhodf$K[NAIVE_GRAPH]),"",  cex.lab=1.5, ylab="Memory (mb)", xlab="Latency(ms)")
        }
        else{
          title(paste(paste("D.",lettere[i]),"EN",lat_report_rhodf$EN[INC_GRAPH],"EW", lat_report_rhodf$EW[NAIVE_GRAPH], "K", lat_report_rhodf$K[NAIVE_GRAPH]),"",  cex.lab=1.5, ylab="Memory (mb)", xlab="Latency(ms)")
          
        }
        
        legend(x=0.3,y=10,pch=pchs, bg = "white", legend=legend, cex=0.5 , col=colors)
        
        dev.off()
        
      }
      
  }
  
  
  print("----END----")

}

lat_report_rhodf <- lat_report_rhodf[with(lat_report_rhodf, order(EN)),]
mem_report_rhodf <- mem_report_rhodf[with(mem_report_rhodf, order(EN)),]
# EW=1 1 2 3 4
# xlim <- c(0.05,5)
# ylim <- c(2.5,10)
# plot_path(c(1,5,9,13), xlim, ylim)


# #EW=10 6 7 8 9
# xlim <- c(0.05,5)
# ylim <- c(2.5,10)
# plot_path(c(21,25,29,33), xlim, ylim)
# #K*EW=10000 9 12 14 15
# xlim <- c(4,5.5)
# ylim <- c(4,8)
# plot_path(c(33,45,53,57), xlim, ylim)
# # #K=1 1 6 10 13 15
xlim <- c(0.05,5)
ylim <- c(2.5,10)
plot_path(c(21,37,57), xlim, ylim)



