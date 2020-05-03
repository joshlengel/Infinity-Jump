#include<Windows.h>
#include<fstream>
#include<iostream>

int WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPTSTR lpCmdLine, int cmdShow)
{
	STARTUPINFOW si;
	PROCESS_INFORMATION pi;

	ZeroMemory(&si, sizeof(si));
	si.cb = sizeof(si);
	ZeroMemory(&pi, sizeof(pi));

	wchar_t command[] = L"javaw --module-path ../lib/JavaFX-13.0.2/lib --add-modules=javafx.controls -jar \"../lib/Infinity Jump/Infinity Jump IDE.jar\" ../lib/assets/";

	int retval = CreateProcessW(NULL, command, NULL, NULL, FALSE, CREATE_NO_WINDOW, NULL, NULL, &si, &pi);

	if (retval)
	{
		WaitForSingleObject(pi.hProcess, INFINITE);
    	CloseHandle(pi.hProcess);
    	CloseHandle(pi.hThread);
	} else {
		std::ofstream log("errorlog.log");
		log << "Error running \"Infinity Jump IDE.jar\". Error code: " << retval;
		std::cout << "Error running \"Infinity Jump IDE.jar\". Error code: " << retval;

		return -1;
	}

	return 0;
}