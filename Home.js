/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */
import React from 'react';
import {Button, Platform, StatusBar, ScrollView, Text, View} from 'react-native';

import TTAdFullScreenVideo from './src/TTAdFullScreenVideo';

class Home extends React.Component {
    constructor() {
        super();
        this.initialize()
    }

    initialize = () => {
        if(Platform.OS==='ios'){
           
            this.videoAdId = "900546299"
            
        }else {
           
            this.videoAdId = "901121375"
            

        }

    }

    componentDidMount(){
        StatusBar.setHidden(true);

        this.eventsFullScreenVideo();
        
    }

    render() {
        return (
            <ScrollView>
                

                <Button
                    title={'全屏视频'}
                    onPress={() => {
                        TTAdFullScreenVideo.showFullScreenVideoAd(this.videoAdId);
                    }}
                />

               

            </ScrollView>
        );
    }

    //全屏视频事件
    eventsFullScreenVideo = () => {
        TTAdFullScreenVideo.addEventListener('fullScreenVideoAdShowed',
            () => console.log('全屏视频展示事件fullScreenVideoAdShowed')
        );
        TTAdFullScreenVideo.addEventListener('fullScreenVideoAdClick',
            () => console.log('点击视频事件fullScreenVideoAdClick')
        );
        TTAdFullScreenVideo.addEventListener('fullScreenVideoAdClose',
            () => console.log('关闭全屏视频事件fullScreenVideoAdClose')
        );
        TTAdFullScreenVideo.addEventListener('fullScreenVideoComplete',
            () => console.log('视频播放完成事件fullScreenVideoComplete')
        );
        TTAdFullScreenVideo.addEventListener('fullScreenVideoSkipped',
            () => console.log('点击跳过视频事件fullScreenVideoSkipped')
        );
        TTAdFullScreenVideo.addEventListener('fullScreenVideoAdFailedToLoad',
            (error) => console.warn('fullScreenVideoAdFailedToLoad视频加载失败:',error.message,error.code)
        );
    };

   

    componentWillUnmount(){
        TTAdFullScreenVideo.removeAllListeners();
        
    }

}

export default Home;
