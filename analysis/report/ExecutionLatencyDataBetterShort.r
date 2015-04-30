#Plot sine Model
#Load Data 
args<-commandArgs(TRUE)
dataset <- read.csv(args[1])
filename = gsub(".csv", "", args[1])
xmean <- rowMeans(cbind(dataset$LAT0,dataset$LAT1 ,dataset$LAT2 ,dataset$LAT3 ,dataset$LAT4,dataset$LAT5,dataset$LAT6,dataset$LAT7,dataset$LAT8,dataset$LAT9))
reportName <- paste(filename,"_better_lat_report_short.csv")
COMMA<-";"


cat("filename", file=reportName,  fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
plotsummary <- summary(xmean[1:4000])
plotsummarySS <- summary(xmean[500:4000])

for(j in seq(1,6,1)){
  cat(names(plotsummary)[j],file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}
cat("Dev Std",file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
for(j in seq(1,6,1)){
  cat(paste(names(plotsummarySS)[j], "SS"),file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
	}
cat("Dev StdSS",file=reportName, fill=FALSE, append=TRUE)
cat("\n", file=reportName,  fill=FALSE, append=TRUE)
cat(filename, file=reportName,  fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
for(j in seq(1,6,1)){
  cat(as.vector(plotsummary)[j],file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
}
cat(sd(plotsummary),file=reportName, fill=FALSE, append=TRUE)
cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
for(j in seq(1,6,1)){
  cat(as.vector(plotsummarySS)[j],file=reportName, fill=FALSE, append=TRUE)
		cat(COMMA, file=reportName,  fill=FALSE, append=TRUE)
	}
cat(sd(plotsummarySS),file=reportName, fill=FALSE, append=TRUE)
cat("\n", file=reportName,  fill=FALSE, append=TRUE)