var path = require('path');

module.exports = {
    entry: './src/main/webapp/index.js',
    devtool: 'source-map',
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: './build/js/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /node_modules/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.css$/i,
                include: path.resolve(__dirname, './src/main/webapp'),
                use: ['style-loader', 'css-loader', 'postcss-loader'],
            }
        ]
    },
    resolve: {
        extensions: ['.js', '.jsx']
    }
};