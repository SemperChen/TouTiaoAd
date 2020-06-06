import {NativeEventEmitter, NativeModules} from 'react-native';
const TTAdFullScreenVideo = NativeModules.TTAdFullScreenVideo;
const eventEmitter = new NativeEventEmitter(TTAdFullScreenVideo);
const eventMap = {
    fullScreenVideoAdShowed: 'fullScreenVideoAdShowed',
    fullScreenVideoAdClick:"fullScreenVideoAdClick",
    fullScreenVideoAdClose:"fullScreenVideoAdClose",
    fullScreenVideoComplete:"fullScreenVideoComplete",
    fullScreenVideoSkipped:"fullScreenVideoSkipped",
    fullScreenVideoAdFailedToLoad:"fullScreenVideoAdFailedToLoad"
};
const _subscriptions = new Map();
const addEventListener = (event, handler) => {
    const mappedEvent = eventMap[event];
    if (mappedEvent) {
        let listener;
        if (event === "fullScreenVideoAdFailedToLoad") {
            listener = eventEmitter.addListener(mappedEvent, error => handler(error));
        } else {
            listener = eventEmitter.addListener(mappedEvent, handler);
        }
        _subscriptions.set(handler, listener);
        return {
            remove: () => removeEventListener(event, handler),
        };
    } else {
        // eslint-disable-next-line no-console
        console.warn(`Trying to subscribe to unknown event: "${event}"`);
        return {
            remove: () => {},
        };
    }
};

const removeEventListener = (type, handler) => {
    const listener = _subscriptions.get(handler);
    if (!listener) {
        return;
    }
    listener.remove();
    _subscriptions.delete(handler);
};

const removeAllListeners = () => {
    _subscriptions.forEach((listener, key, map) => {
        listener.remove();
        map.delete(key);
    });
};

export default {
    ...TTAdFullScreenVideo,
    addEventListener,
    removeEventListener,
    removeAllListeners,
};
