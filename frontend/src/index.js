import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {AppProvider} from 'AppContext';
import './index.scss';
import 'antd/dist/antd.css';
import 'semantic-ui-css/semantic.min.css';

ReactDOM.render(
    <React.StrictMode>
        <AppProvider>
            <App/>
        </AppProvider>
    </React.StrictMode>,
    document.getElementById('root')
);
