package de.Luca.EventManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.Luca.Events.Event;

public class EventManager {
	
	//eine statische klasse, die die Events und die Eventlistener verwaltet
	//Dieser Code ist größteneteils vom Eventsystem von Spigot (Minecraftserver) kopiert
		
	private static ConcurrentHashMap<Listener, HashMap<Class<? extends Event>, List<Method>>> listeners = new ConcurrentHashMap<Listener, HashMap<Class<? extends Event>, List<Method>>>();
	
	
	//Es können Listener hinzugefügt werden, in welchen deklaiert werden kann, was bei bestimmten Events passiert.
	public static void registerEvent(Listener listener) {
		listeners.put(listener, createRegisteredListener(listener));
	}
	
	private static void fireEvent(Event e, EventPriority p) {
		final Class<? extends Event> eventClass = e.getClass().asSubclass(Event.class);
		for(Listener l : listeners.keySet()) {
			if(listeners == null || l == null || eventClass == null) {
				continue;
			}
			try {
				if(listeners.get(l).containsKey(eventClass)) {
					for(Method m : listeners.get(l).get(eventClass)) {
						if(m.getAnnotation(EventHandler.class).priority() == p) {
							m.invoke(l, e);
						}
					}
				}
			}catch (Exception ex) {}
		}
	}
	
	public static void fireEvent(Event e) {
		fireEvent(e, EventPriority.HIGH);
		fireEvent(e, EventPriority.NORMAL);
		fireEvent(e, EventPriority.LOW);
	}
	
	//Adds all Methods with annotation "EventHandler" to List
	private static HashMap<Class<? extends Event>, List<Method>> createRegisteredListener(Listener listener) {
		HashMap<Class<? extends Event>, List<Method>> methods = new HashMap<Class<? extends Event>, List<Method>>();
		Method[] publicMethod = listener.getClass().getMethods();
		for(Method m : publicMethod) {
			if(m.getAnnotation(EventHandler.class) != null) {
				if(!m.isBridge() && !m.isSynthetic()) {
					if(m.getParameterTypes().length == 1) {
						final Class<?> checkClass;
						if(Event.class.isAssignableFrom(checkClass = m.getParameterTypes()[0])) {
							 final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
							 m.setAccessible(true);
							 
							 if(!methods.containsKey(eventClass)) {
								 List<Method> l = new ArrayList<Method>();
								 methods.put(eventClass, l);
							 }
							 List<Method> l = methods.get(eventClass);
							 l.add(m);
						}
					}
				}
			}
		}
		return methods;
	}
	
	//Es können Listener entfernt werden.
	public static void removeListener(Listener listener) {
		listeners.remove(listener);
	}

}
