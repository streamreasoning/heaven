#Plot the latency model, first argument is the upperbound, the the dataset containing the plotting data
#Load Data 
#plot(lat_report_rhodf$Median.SS ~ mem_report_rhodf$A..Median.SS)
#args<-commandArgs(TRUE)
#lat_report_rhodf <- read.csv(args[1])
#mem_report_rhodf <- read.csv(args[2])
dashboard<- function(a,b,c,d){
  
  exp <- paste(lat_report_rhodf$STREAM[a], substr(lat_report_rhodf$REASONING[a], 0, 3))
  
  
  plot(seq(0,250,5),seq(0,800,16),  type = "n", ylab = "Memory", xlab = "Latency")
  legend(x=0,y=800,pch=c(0,15,1,16,16),bg = "white", legend=c(
    paste(exp, "K" ,lat_report_rhodf$K[a],"EW", lat_report_rhodf$EW[a]), 
    paste(exp, "K" ,lat_report_rhodf$K[b], "EW",lat_report_rhodf$EW[b]), 
    paste(exp, "K" ,lat_report_rhodf$K[c], "EW",lat_report_rhodf$EW[c]), 
    paste(exp, "K" ,lat_report_rhodf$K[d], "EW",lat_report_rhodf$EW[d])), 
    cex=1 ,y.intersp=1, col=c("red","blue","black","green"))
  title(exp, paste("ENs", lat_report_rhodf$EN[a], lat_report_rhodf$EN[b], lat_report_rhodf$EN[c], lat_report_rhodf$EN[d]))
  abline( h = seq( 0, 800, 10 ), lty = 3, col = colors()[ 440 ] )
  abline( v = seq( 0, 800, 10 ), lty = 3, col = colors()[ 440 ] )
  
  
  #Graph Naive
  z<-a
  x <- lat_report_rhodf$Median.SS[z]
  y <- mem_report_rhodf$A..Median.SS[z]
  
  
  par(new=TRUE)
  points(x = x, y = y, col="red", type = "p", pch=0)
  arrows(x, y-mem_report_rhodf$A.Dev.Std.SS[z],x,y+mem_report_rhodf$A.Dev.Std.SS[z], length=0.05, angle=90, code=3)
  arrows(x-lat_report_rhodf$Dev.StdSS[z], y,x+lat_report_rhodf$Dev.StdSS[z],y, length=0.05, angle=90, code=3)

  #text(x=x,y=y, labels = lat_report_rhodf$EN[SR11])
  #stmt naive
  z<-b
  x <- lat_report_rhodf$Median.SS[z]
  y <- mem_report_rhodf$A..Median.SS[z]
  #text(x=x,y=y, labels = lat_report_rhodf$EN[SR110])
  
  par(new=TRUE)
  points(x = x, y = y, col="blue", type = "p", pch=15)
  arrows(x, y-mem_report_rhodf$A.Dev.Std.SS[z],x,y+mem_report_rhodf$A.Dev.Std.SS[z], length=0.05, angle=90, code=3)
  arrows(x-lat_report_rhodf$Dev.StdSS[z], y,x+lat_report_rhodf$Dev.StdSS[z],y, length=0.05, angle=90, code=3)
  
  #Graph inc
  z<-c
  x <- lat_report_rhodf$Median.SS[z]
  y <- mem_report_rhodf$A..Median.SS[z]
  #text(x=x,y=y, labels = lat_report_rhodf$EN[SR1100])
  
  par(new=TRUE)
  points(x = x, y = y, col="black", type = "p", pch=1)
  arrows(x, y-mem_report_rhodf$A.Dev.Std.SS[z],x,y+mem_report_rhodf$A.Dev.Std.SS[z], length=0.05, angle=90, code=3)
  arrows(x-lat_report_rhodf$Dev.StdSS[z], y,x+lat_report_rhodf$Dev.StdSS[z],y, length=0.05, angle=90, code=3)
  
  
  #stmt inc
  z <- d
  x <- lat_report_rhodf$Median.SS[z]
  y <- mem_report_rhodf$A..Median.SS[z]
  
  par(new=TRUE)
  points(x = x, y = y, col="green", type = "p", pch=16)
  arrows(x, y-mem_report_rhodf$A.Dev.Std.SS[z],x,y+mem_report_rhodf$A.Dev.Std.SS[z], length=0.05, angle=90, code=3)
  arrows(x-lat_report_rhodf$Dev.StdSS[z], y,x+lat_report_rhodf$Dev.StdSS[z],y, length=0.05, angle=90, code=3)
  #text(x=x,y=y, labels = lat_report_rhodf$EN[SR11000])
}

png("Dashboards All Baselines EW=1 .png", 1000, 1000)


SR11 <- 1
SR110 <- 5
SR1100 <- 9
SR11000 <- 13
#SR110000 <- 17
split.screen(c(2,1))
split.screen(c(1,2), screen=1)
screen(3)
#NAIVE GRAPH
dashboard(SR11,SR110,SR1100,SR11000)
screen(4)
#NAIVE STMT
dashboard(SR11+1,SR110+1,SR1100+1,SR11000+1)
split.screen(c(1,2), screen=2)
screen(5)
#INC GRAPH
dashboard(SR11+2,SR110+2,SR1100+2,SR11000+2)
screen(6)
#INC STMT
dashboard(SR11+3,SR110+3,SR1100+3,SR11000+3)

dev.off()

png("Dashboards All Baselines WINSIZE=10000 .png", 1000, 1000)


a <- 33
b <- 45
c <- 53
d <- 57
#SR110000 <- 17
split.screen(c(2,1))
split.screen(c(1,2), screen=1)
screen(3)
#NAIVE GRAPH
dashboard(a,b,c,d)
screen(4)
#NAIVE STMT
dashboard(a+1,b+1,c+1,d+1)
split.screen(c(1,2), screen=2)
screen(5)
#INC GRAPH
dashboard(a+2,b+2,c+2,d+2)
screen(6)
#INC STMT
dashboard(a+3,b+3,c+3,d+3)

dev.off()








