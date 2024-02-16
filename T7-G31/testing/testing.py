import json
import requests

url = "http://localhost:1234/compile-and-codecoverage"

class ResponseDTO:
    def __init__(self, error, outCompile, coverage):
        self.error = error
        self.outCompile = outCompile
        self.coverage = coverage

    def __repr__(self):
        return f'ResponseDTO(error={self.error}, outCompile={self.outCompile}, coverage={self.coverage})'

def read_files_as_strings(file1_path, file2_path):
    with open(file1_path, 'r') as file1, open(file2_path, 'r') as file2:
        file1_string = file1.read()
        file2_string = file2.read()

    return file1_string, file2_string

for i in range(10):
    file1,file2 = read_files_as_strings("TestFontInfo.java", "FontInfo.java")
    payload = {"testingClassName": "TestFontInfo.java",
    "testingClassCode": file1,
    "underTestClassName": "FontInfo.java",
    "underTestClassCode": file2}
    headers = {'Content-type': 'application/json'}
    response = requests.post(url, data=json.dumps(payload), headers=headers)
    response_dict = json.loads(response.text)
    response_dto = ResponseDTO(response_dict['error'], response_dict['outCompile'], response_dict['coverage'])
    print(response_dto)
    file1,file2 = read_files_as_strings("TestChunkedLongArray.java", "ChunkedLongArray.java")
    payload = {"testingClassName": "TestChunkedLongArray.java",
    "testingClassCode": file1,
    "underTestClassName": "ChunkedLongArray.java",
    "underTestClassCode": file2}
    