

CALL pos.d_gen_reference('TimeKeeping Type', 'Loại chấm công',
                         '[
                           {
                             "referenceListValue": "CIN",
                             "referenceListName": "Chấm công vào",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "COT",
                             "referenceListName": "Chấm công ra",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "ACC",
                             "referenceListName": "Bổ sung chấm công ",
                             "lineNo": 2
                           }
                         ]'::jsonb);



CALL pos.d_gen_reference('Type of work', 'Loại công',
                         '[
                           {
                             "referenceListValue": "REG",
                             "referenceListName": "Công thường",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "OTR",
                             "referenceListName": "Tăng ca",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "BUS",
                             "referenceListName": "Công tác",
                             "lineNo": 2
                           }
                         ]'::jsonb);




CALL pos.d_gen_reference('TimeSheet  Status', 'Trạng thái công',
                         '[
                           {
                             "referenceListValue": "ONT",
                             "referenceListName": "Đúng giờ",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "LAT",
                             "referenceListName": "Trễ giờ",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "ABS",
                             "referenceListName": "Vắng mặt",
                             "lineNo": 2
                           }
                         ]'::jsonb);
