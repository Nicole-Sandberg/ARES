# ARES
Service that search through swedish årsredovisningar that has been ocr-scanned.

---
The project use Spring and Elasticsearch. It uses QueryStringQueryBuilder for SalesInsight to parse input after "and", "or" and "not". It manualy search for a costumer that wants a excel-file with the report that matched the customers keywords, order by amount of hits.
