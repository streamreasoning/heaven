#Plot sine Model
#Load Data 
args<-commandArgs(TRUE)
dataset <- read.csv(args[1])
filename = gsub(".csv", "", args[1])
reportName <- paste(filename, "_better_mem_report.csv")
COMMA<-";"

xmean <- rowMeans(cbind(dataset$MEMB0,dataset$MEMB1 ,dataset$MEMB2 ,dataset$MEMB3 ,dataset$MEMB4,dataset$MEMB5,dataset$MEMB6,dataset$MEMB7,dataset$MEMB8,dataset$MEMB9))
plotsummary <- summary(xmean)
plotsummarySS <- summary(xmean[3000:2000])

cat("filename", file=reportName,  fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
for(j in seq(1,6,1)){
  cat(paste("B ",names(plotsummary)[j]),file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}

cat("B Dev Std",file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)

for(j in seq(1,6,1)){
  cat(paste("B ",paste(names(plotsummary)[j], "SS")),file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
	}
cat("B Dev Std SS",file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)

for(j in seq(1,6,1)){
  cat(paste("A ",names(plotsummary)[j]),file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}

cat("A Dev Std",file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)

for(j in seq(1,6,1)){
  cat(paste("A ",paste(names(plotsummarySS)[j], "SS")),file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
	}
cat("A Dev Std SS",file=reportName, fill=FALSE, append=TRUE)
cat("\n", file=reportName,  fill=FALSE, append=TRUE)

cat(filename, file=reportName,  fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
for(j in seq(1,6,1)){
  cat(as.vector(plotsummary)[j],file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}
cat(sd(plotsummary, FALSE),file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)

for(j in seq(1,6,1)){
  cat(as.vector(plotsummarySS)[j],file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}
cat(sd(plotsummarySS, FALSE),file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)

xmean <- rowMeans(cbind(dataset$MEMA0,dataset$MEMA1 ,dataset$MEMA2 ,dataset$MEMA3 ,dataset$MEMA4,dataset$MEMA5,dataset$MEMA6,dataset$MEMA7,dataset$MEMA8,dataset$MEMA9))
	

plotsummary <- summary(xmean)
plotsummarySS <- summary(xmean[3000:2000])

for(j in seq(1,6,1)){
  cat(as.vector(plotsummary)[j],file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}
cat(sd(plotsummary, FALSE),file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)

for(j in seq(1,6,1)){
  cat(as.vector(plotsummarySS)[j],file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}
cat(sd(plotsummarySS, FALSE),file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
cat("\n", file=reportName,  fill=FALSE, append=TRUE)

