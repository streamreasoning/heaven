from sys import argv
import csv
import operator

def getStreamType(stream, types):
    for t in types:
            if t not in s.lower():
                continue
            else:
                return t

# end

output = argv[1]
streams = argv[2:]
print("Streams: ", str(streams))
final_header_length = 0

citybench_stream_timestamp_position = {
        'pollution': 7,
        'traffic': 5,
        'parking': 5,
        'user_location': 0,
        'weather': 3
    }
stream_descriptors = {}

for s in streams:
    with open(s, 'rb') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')
        t = getStreamType(s, citybench_stream_timestamp_position.keys())
        pos = citybench_stream_timestamp_position[t]
        header = reader.next()
        final_header_length = max(final_header_length, len(header))

        stream_descriptors[s] = {
            'name': s,
            'header': header,
            'type': t,
            'timeStampPos': pos,
            'data': sorted(reader, key=operator.itemgetter(pos), reverse=False)}

with open('temp.stream', 'w') as csvfile:
    writer = csv.writer(csvfile, delimiter=',')
    for s in stream_descriptors.keys():
        strm = stream_descriptors[s]
        for line in strm['data']:
            if strm['timeStampPos'] == final_header_length:
                writer.writerow(line)
            else:
                timestamp = line[strm['timeStampPos']]
                line.remove(timestamp)
                old_line_length = len(line)+1
                for i in range(0, (final_header_length - old_line_length)):
                    line.append("-")
                line.append(strm['name'])
                line.append(strm['type'])
                line.append(timestamp.replace("T", " "))
                writer.writerow(line)

with open('temp.stream', 'rb') as temp:
    final = open(output, 'w')
    reader = csv.reader(temp, delimiter=',')
    writer = csv.writer(final, delimiter=',')
    writer.writerow(['Field_' + str(i) for i in range(0, final_header_length+2)])
    for line in sorted(reader, key=operator.itemgetter(-1), reverse=False):
        writer.writerow(line)
