from PyQt5 import QtWidgets
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import sys
from project import *
from explain import *

# from annotation import process, init_conn


class ScrollableLabel(QScrollArea):

    def __init__(self, *args, **kwargs):
        QScrollArea.__init__(self, *args, **kwargs)
        widget = QWidget(self)
        self.setWidgetResizable(True)
        self.setWidget(widget)
        layout = QVBoxLayout(widget)
        self.label = QLabel(widget)
        self.label.setAlignment(Qt.AlignLeft | Qt.AlignTop)
        self.label.setFont(QFont('Arial', 12))
        self.label.setStyleSheet(
            "background-color: beige; border: 1px solid black;")
        # self.input_sql = self.findChild(QTextEdit, "input_query")
        # self.label_qep = self.findChild(QLabel, "text_plan")
        # self.btn_analyse = self.findChild(QPushButton, "btn_analyse")
        # self.btn_clear = self.findChild(QPushButton, "btn_clear")
        # self.list_database = self.findChild(QComboBox, "combo_databases")
        # self.tree_attrs = self.findChild(QTreeWidget, "tree_attrs")
        layout.addWidget(self.label)

    def setText(self, text):
        self.label.setText(text)


class MyWindow(QMainWindow):
    def __init__(self):
        super(MyWindow, self).__init__()
        self.setGeometry(50, 50, 1800, 1000)  # xpos, ypos, width, height
        self.setWindowTitle("CZ4031-Assignment2-Group 31")

        # Output text for query and annotation
        self.queryOutput1 = ScrollableLabel(self)
        self.queryOutput2 = ScrollableLabel(self)
        self.queryExplain = ScrollableLabel(self)

        # Button for running algorithm
        self.submitButton = QtWidgets.QPushButton(self)

        # Textbox for query and db name
        self.queryTextbox1 = QTextEdit(self)
        self.queryTextbox2 = QTextEdit(self)
        self.dbNameTextbox = QTextEdit(self)

        # Label to indicate which db name is the app currently connected to
        self.dbNameLabel = ScrollableLabel(self)

        # Error message box
        self.error_dialog = QtWidgets.QErrorMessage()

        self.UI()  # Call initUI

        # Db connection settings
        self.database = None
        self.dbName = ''

    def UI(self):
        self.queryTextbox1.move(30, 5)
        self.queryTextbox1.resize(700, 150)
        self.queryTextbox1.setFont(QFont('Arial', 12))
        self.queryTextbox1.setPlaceholderText("Insert SQL Query Q1 Here :")

        self.queryTextbox2.move(740, 5)
        self.queryTextbox2.resize(700, 150)
        self.queryTextbox2.setFont(QFont('Arial', 12))
        self.queryTextbox2.setPlaceholderText("Insert SQL Query Q2 Here :")

        self.dbNameTextbox.move(1500, 5)
        self.dbNameTextbox.resize(270, 150)
        self.dbNameTextbox.setFont(QFont('Arial', 12))
        self.dbNameTextbox.setPlaceholderText("Insert Database Name Here :")

        self.queryOutput1.setText("Output Query Q1 :")
        self.queryOutput1.move(30, 170)
        self.queryOutput1.resize(700, 400)
        self.queryOutput1.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryOutput1.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryOutput2.setText("Output Query Q2 :")
        self.queryOutput2.move(740, 170)
        self.queryOutput2.resize(700, 400)
        self.queryOutput2.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryOutput2.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryExplain.setText("Explain for Output Query :")
        self.queryExplain.move(30, 580)
        self.queryExplain.resize(1410, 400)
        self.queryExplain.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryExplain.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryOutput1.verticalScrollBar().valueChanged.connect(
            self.queryExplain.verticalScrollBar().setValue)
        self.queryExplain.verticalScrollBar().valueChanged.connect(
            self.queryOutput1.verticalScrollBar().setValue)

        self.queryOutput2.verticalScrollBar().valueChanged.connect(
            self.queryExplain.verticalScrollBar().setValue)
        self.queryExplain.verticalScrollBar().valueChanged.connect(
            self.queryOutput2.verticalScrollBar().setValue)

        self.dbNameLabel.move(1500, 170)
        self.dbNameLabel.resize(270, 100)
        self.dbNameLabel.setText(f"Current Database Name: ")

        self.submitButton.setText("Submit Query")
        self.submitButton.setFont(QFont('Arial', 12))
        self.submitButton.clicked.connect(self.onClick)
        self.submitButton.move(1500, 700)
        self.submitButton.resize(270, 100)

    def onClick(self):
        if self.dbName != self.dbNameTextbox.toPlainText():
            try:
                # A connection in need of explain.py's obtain message function
                self.database = Database(self.dbNameTextbox.toPlainText())
                self.dbName = self.dbNameTextbox.toPlainText()
                self.dbNameLabel.setText(f"Current DB Name: {self.dbName}")
            except Exception as e:
                self.error_dialog.showMessage(f"ERROR - {e}")
        if self.database is not None:
            try:
                # A connection in need of explain.py's output result function
                # def explain(self, query, query2):
                # Query 1
                #query, annotation = process(
                #    self.database, self.queryTextbox1.toPlainText())
                #self.queryOutput1.setText('\n'.join(query))
                #self.queryExplain.setText('\n'.join(annotation))
                explain_class = explain()
                explanation = explain_class.explain(self.database, self.queryTextbox1.toPlainText(), self.queryTextbox2.toPlainText())
                self.queryExplain.setText(explanation)
                # # Query 2
                # query, annotation = process(self.database, self.queryTextbox2.toPlainText())
                # self.queryOutput2.setText('\n'.join(query))
                # self.queryExplain.setText('\n'.join(annotation))
                # print(annotation[0][0])
            except Exception as e:
                self.error_dialog.showMessage(f"ERROR - {e}")

def process(database, query):
    query_results = database.get_query_results(query)
    final_query_result = []
    for i in query_results:
        temp = ""
        for j in i:
            temp += str(j)+"|"
        final_query_result.append(temp)
    raw_explanation = database.get_query_results(
        "explain (analyze true , format json)"+query)
    print(raw_explanation[0][0][0])
    print("--------------")
    print(raw_explanation[0][0][0]['Plan']['Plans'][0])
    print("--------------")
    print(raw_explanation[0][0][0]['Plan']['Plans'][1])
    print("--------------")
    print(raw_explanation[0][0][0]['Plan']['Plans'][1]['Plans'])

    for Plans in raw_explanation[0][0]:
        print("i am working")
        print(Plans['Plan']['Node Type'])
        for PlansIns in Plans['Plan']['Plans']:
            print(PlansIns['Node Type'])
            # if no plans skip  .. unsure how to implement.
            if (PlansIns['Plans'] == null):
                continue
            for x in Plans['Plans']:
                print(x)

        # for PlansIns in Plans['Plans']:
        #   print(PlansIns['Node Type'])

    final_raw_explanation = []
    for i in raw_explanation:
        final_raw_explanation.append(i[0])
    return final_query_result, final_raw_explanation
    # def init_conn(db_name):
    #    db_uname, db_pass, db_host, db_port = import_config()
    #    conn = open_db(db_name, db_uname, db_pass, db_host, db_port)
    #    return conn

    # def showError(self, errMessage, execption=None):
    #    dialog = QMessageBox()
    #    dialog.setStyleSheet("QLabel{min-width: 300px;}");
    #    dialog.setWindowTitle("Error")
    #    #dialog.setIcon(QMessageBox.Warning)
    #    dialog.setText(errMessage)
    #    if execption is not None:
    #        dialog.setDetailedText(str(execption))
    #    dialog.setStandardButtons(QMessageBox.Ok)
    #    #dialog.buttonClicked.connect(cb)
    #    dialog.exec_()


def window():
    app = QApplication(sys.argv)
    win = MyWindow()

    win.show()
    sys.exit(app.exec_())


def main():
    window()


if __name__ == '__main__':
    main()
