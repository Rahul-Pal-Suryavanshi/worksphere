import axiosClient from "./api/axiosClient";

function App() {

    console.log(axiosClient.defaults.baseURL);

    return (
        <div className="min-h-screen flex items-center justify-center bg-slate-100">
        <h1 className="text-5xl font-bold text-blue-600">WorkSphere Frontend</h1>
        </div>
    );
}
export default App;